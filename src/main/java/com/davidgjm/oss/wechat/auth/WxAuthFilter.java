package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.ApiErrorDetails;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.wxsession.WxSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This filter checks the value of HTTP header <code>skey</code>.
 * The filter chain will pass on if the skey is valid and a registered user exists in the system.
 * Otherwise, this filter will fail.
 */
@Slf4j
@Component
public class WxAuthFilter extends OncePerRequestFilter {
    private final WxSessionService sessionService;
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Value("#{'${config.whitelist-url}'.split(',')}")
    private List<String> whitelistUrls;

    public WxAuthFilter(WxSessionService sessionService) {
        this.sessionService = sessionService;
    }

    public List<String> getWhitelistUrls() {
        return whitelistUrls;
    }

    public void setWhitelistUrls(List<String> whitelistUrls) {
        this.whitelistUrls = whitelistUrls;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uri=request.getRequestURI();
        String method = request.getMethod();
        String requestInfo = method + " " + uri;
        log.debug("REST API request: {}", requestInfo);
        String skey = request.getHeader("skey");
        log.info("Validating skey {}", skey);
        if (skey == null || skey.isEmpty()) {
            log.error("Header \"skey\" is required!");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "skey is required!");
            return;
        }


        //check if session valid first
        log.debug("Validating session...");
        Optional<WxSession> sessionOptional = validateSession(skey);
        if (!sessionOptional.isPresent()) {
            log.error("User not found with the skey!");
            handleException(response, HttpStatus.BAD_REQUEST, new WxSkeyNotFoundException("Skey not found"));
            return;
        }
        log.debug("session ID: {}", sessionOptional.get().getId());

        log.debug("skey is valid and corresponding user exists. {}", skey);
        chain.doFilter(request, response);
    }

    private Optional<WxSession> validateSession(String skey) {
        return sessionService.findBySkey(skey);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri=request.getRequestURI();
        String method = request.getMethod();
        String requestInfo = method + ":" + uri;
        if (whitelistUrls==null || whitelistUrls.isEmpty()) return false;

        return whitelistUrls.parallelStream().anyMatch(requestInfo::matches);
    }

    private void handleException(HttpServletResponse response, HttpStatus status, Exception e) throws IOException {
        ApiErrorDetails errorDetails = ApiErrorDetails.from(status, e);
        response.setStatus(status.value());
        response.getWriter().write(toJson(errorDetails));
    }

    private String toJson(ApiErrorDetails errorDetails) throws JsonProcessingException {
        return objectMapper.writeValueAsString(errorDetails);
    }

}
