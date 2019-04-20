package com.davidgjm.oss.wechat.crypto;

import com.davidgjm.oss.wechat.base.util.ParamChecker;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.wxuserinfo.WxUserInfoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class WxCryptoServiceImpl implements WxCryptoService {

    private final ObjectMapper objectMapper;
    private DigestUtil digestUtil = DigestUtil.HASH_SHA1;
    private GeneralAesCipher generalAesCipher;

    public WxCryptoServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void init() {
        this.generalAesCipher = new GeneralAesCipher(GeneralAesCipher.Algorithm.CBC_PKCS7);
    }

    @Override
    public WxUserInfoDTO decryptUserInfo(@NotBlank String sessionKey, WxEncryptedData encryptedUserInfo) {
        try {
            String decryptedContent = decrypt(sessionKey, encryptedUserInfo);
            log.debug("decrypted data:\n{}", decryptedContent);
            return objectMapper.readValue(decryptedContent, WxUserInfoDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String decryptPhoneNumber(@NotBlank String sessionKey, WxEncryptedData encryptedData) {
        try {
            JsonNode jsonNode = objectMapper.readTree(decrypt(sessionKey, encryptedData));
            return jsonNode.get("phoneNumber").asText();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to decrypt phone number.", e);
            throw new IllegalArgumentException(e);
        }
    }

    private <T extends WxEncryptedData> String decrypt(String secret, T wxEncryptedData) {
        byte[] secretData = Base64.getDecoder().decode(secret);
        byte[] encryptedData = Base64.getDecoder().decode(wxEncryptedData.getEncryptedData());
        byte[] iv = Base64.getDecoder().decode(wxEncryptedData.getIv());

        generalAesCipher.init(secretData, iv);
        try {
            byte[] decrypted = generalAesCipher.decrypt(encryptedData);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new WxSecurityException(e);
        }
    }

    @Override
    public void encryptSessionKey(WxSession session) {
        ParamChecker.checkArg(session.getSessionKey(),"Session key is required!" );
        session.setSkey(digestUtil.hash(session.getSessionKey()));
    }

    @Override
    public void validateSignature(String providedSignature, String rawData, String sessionKey) {
        String computedSignature = computeSignature(rawData, sessionKey);
        if (!computedSignature.equals(providedSignature)) {
            log.error("Signature validation failed. \nsignature: [{}]\ncomputed: [{}]", providedSignature, computedSignature);
            throw new WxSecurityException("Signature validation failed");
        }
    }

    private String computeSignature( String rawData, String sessionKey) {
        return digestUtil.hash(rawData + sessionKey);
    }
}
