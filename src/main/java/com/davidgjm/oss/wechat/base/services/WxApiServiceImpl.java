package com.davidgjm.oss.wechat.base.services;

import com.davidgjm.oss.wechat.crypto.WxCryptoService;
import com.davidgjm.oss.wechat.base.GenericWxException;
import com.davidgjm.oss.wechat.auth.WxAccessTokenException;
import com.davidgjm.oss.wechat.auth.WxAuthException;
import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessageException;
import com.davidgjm.oss.wechat.base.SimpleCacheKey;
import com.davidgjm.oss.wechat.base.WxError;
import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessage;
import com.davidgjm.oss.wechat.base.util.ParamChecker;
import com.davidgjm.oss.wechat.base.AbstractWxApiResponse;
import com.davidgjm.oss.wechat.auth.WxApiAccessTokenResponse;
import com.davidgjm.oss.wechat.wxsession.WxApiSessionResponse;
import com.davidgjm.oss.wechat.templatemessage.WxApiTemplateMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WxApiServiceImpl implements WxApiService {
    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;
    private final Map<String, String> params = new HashMap<>();
    private final WxCryptoService cryptoService;
    private RefreshableCacheService cacheService;

    @Value("${wx.api.access-token}")
    private String accessTokenUrl;

    @Value("${wx.api.template-message}")
    private URI templateMessageUrl;

    @Value("${wx.mini-program.appid}")
    private String miniProgramAppid;

    @Value("${wx.mini-program.secret}")
    private String miniProgramSecret;

    @Value("${wx.mini-program.grantType}")
    private String grantType;

    @Value("${wx.mini-program.authUrl}")
    private String authUrl;


    public WxApiServiceImpl(ObjectMapper objectMapper, RestTemplate restTemplate, WxCryptoService cryptoService) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.cryptoService = cryptoService;
    }

    @PostConstruct
    private void init() {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Autowired
    public void setCacheService(RefreshableCacheService cacheService) {
        this.cacheService = cacheService;
    }

    private UriComponentsBuilder getBaseUrlBuilder() {
        return UriComponentsBuilder
                .fromUriString(authUrl)
                ;
    }

    private UriComponentsBuilder buildGetSessionUri(String loginCode) {
        return getBaseUrlBuilder()
                .queryParam(QUERY_PARAM_APP_ID, miniProgramAppid)
                .queryParam(QUERY_PARAM_APP_SECRET, miniProgramSecret)
                .queryParam(QUERY_PARAM_GRANT_TYPE, grantType)
                .queryParam(QUERY_PARAM_JS_CODE, loginCode)
                ;
    }

    @Override
    public WxApiSessionResponse getSession(String loginCode) {
        ParamChecker.checkArg(loginCode);
        UriComponentsBuilder uriComponentsBuilder = buildGetSessionUri(loginCode);
        String fullUri = uriComponentsBuilder.toUriString();

        ResponseEntity<WxApiSessionResponse> responseEntity = restTemplate.execute(
                fullUri, HttpMethod.GET, null, response ->
                        new ResponseEntity<>(parseResponse(response.getBody()),response.getStatusCode()));

        WxApiSessionResponse sessionResponse = responseEntity.getBody();
        log.info("API Response: {}", responseEntity.getStatusCode());
        log.debug("Session response details: {}", sessionResponse);
        return sessionResponse;
    }


    private WxApiSessionResponse parseResponse(InputStream inputStream) {
        WxApiSessionResponse sessionResponse = null;
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {

            ;
            StringBuilder stringBuilder = new StringBuilder();
            bufferedReader.lines().forEach(stringBuilder::append);
            bufferedReader.close();


            //handling response text
            String responseText = stringBuilder.toString();
            log.debug("Response text:\n{}", responseText);
            if (responseText.contains("openid")) {
                sessionResponse = objectMapper.readValue(responseText, WxApiSessionResponse.class);
            } else {
                log.error("Get session key failed. {}", responseText);
                WxError error = objectMapper.readValue(responseText, WxError.class);
                log.warn("Error from remote API: {}", error);
                throw new WxAuthException(error.getErrmsg());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }

        return sessionResponse;
    }

    @Override
    public WxApiAccessTokenResponse getAccessToken() {
        log.debug("Getting access token...");
        WxApiAccessTokenResponse tokenResponse= cacheService.getCachedItem(SimpleCacheKey.WX_ACCESS_TOKEN.getKey(),
                WxApiAccessTokenResponse.class,
                token -> LocalDateTime.now().isAfter(token.getExpiresBy()),
                this::doRequestToken);
        ZoneOffset timezone = ZoneOffset.ofHours(8);
        tokenResponse.setExpiresIn(tokenResponse.getExpiresBy().toEpochSecond(timezone) - LocalDateTime.now().toEpochSecond(timezone));
        return tokenResponse;
    }

    @Override
    public WxApiTemplateMessageResponse sendTemplateMessage(@NotNull @Valid WxTemplateMessage templateMessage) {
        log.debug("Getting access token...");
        WxApiAccessTokenResponse tokenResponse=getAccessToken();
        UriComponentsBuilder componentsBuilder = UriComponentsBuilder.fromUri(templateMessageUrl)
                .queryParam(QUERY_PARAM_ACCESS_TOKEN, tokenResponse.getAccessToken());

        HttpEntity<WxTemplateMessage> request = new HttpEntity<>(templateMessage);
        ResponseEntity<WxApiTemplateMessageResponse> responseEntity = restTemplate.postForEntity(componentsBuilder.toUriString(), request, WxApiTemplateMessageResponse.class);

        handleResponse(responseEntity, WxTemplateMessageException.class);
        return responseEntity.getBody();
    }

    private WxApiAccessTokenResponse doRequestToken() {
        log.debug("Getting WeChat open-wxapi access token...");
        ResponseEntity<WxApiAccessTokenResponse> responseEntity = restTemplate.getForEntity(getAccessTokenBuilder().toUriString(), WxApiAccessTokenResponse.class);
        if (responseEntity.getStatusCodeValue() != 200 || !responseEntity.hasBody()) {
            log.error("Error requesting access token. {}", responseEntity.getStatusCode().getReasonPhrase());
            throw new WxAccessTokenException("Error requesting access token.");
        }

        WxApiAccessTokenResponse tokenResponse = responseEntity.getBody();
        handleResponse(responseEntity, WxAccessTokenException.class);
        tokenResponse.setExpiresBy(LocalDateTime.now().plusSeconds(tokenResponse.getExpiresIn()));
        log.debug("Token requested successfully. Expires in {} seconds", tokenResponse.getExpiresIn());
        return tokenResponse;
    }

    private <R extends AbstractWxApiResponse, E extends GenericWxException> void handleResponse(ResponseEntity<R> responseEntity, Class<E> exception) {
        R response = responseEntity.getBody();
        assert response != null;
        if (response.getErrcode() !=null && response.getErrcode() != 0) {
            log.error("Error response: {}", response.getErrmsg());
            try {
                throw exception.getConstructor(String.class).newInstance(String.format("Error: %s, Reason: %s", response.getErrcode().toString(), response.getErrmsg()));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                throw new IllegalStateException(e1);
            }
        }
    }


    private UriComponentsBuilder getAccessTokenBuilder() {
        return UriComponentsBuilder.fromHttpUrl(accessTokenUrl)
                .queryParam(QUERY_PARAM_GRANT_TYPE,"client_credential")
                .queryParam(QUERY_PARAM_APP_ID, miniProgramAppid)
                .queryParam(QUERY_PARAM_APP_SECRET, miniProgramSecret)
                ;
    }

    private static final String QUERY_PARAM_ACCESS_TOKEN = "access_token";
    private static final String QUERY_PARAM_GRANT_TYPE = "grant_type";
    private static final String QUERY_PARAM_APP_ID = "appid";
    private static final String QUERY_PARAM_APP_SECRET = "secret";
    private static final String QUERY_PARAM_JS_CODE = "js_code";
}
