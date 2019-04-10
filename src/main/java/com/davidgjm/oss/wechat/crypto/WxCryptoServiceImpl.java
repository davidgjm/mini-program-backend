package com.davidgjm.oss.wechat.crypto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.davidgjm.oss.wechat.base.util.ParamChecker;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.auth.WxLoginDTO;
import com.davidgjm.oss.wechat.wxuser.WxUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
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
    public WxUserInfoDTO decryptUserInfo(@NotNull @Validated WxSession session, WxLoginDTO wxLoginDTO) {
        try {
            String decryptedContent = decrypt(session, wxLoginDTO);
            log.debug("decrypted data:\n{}", decryptedContent);
            return objectMapper.readValue(decryptedContent, WxUserInfoDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String decryptPhoneNumber(@NotNull @Validated WxSession session, WxEncryptedData encryptedData) {
        try {
            JsonNode jsonNode = objectMapper.readTree(decrypt(session, encryptedData));
            return jsonNode.get("phoneNumber").asText();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to decrypt phone number.", e);
            throw new IllegalArgumentException(e);
        }
    }

    private String decrypt(WxSession session, WxEncryptedData wxEncryptedData) {
        byte[] sessionKey = Base64.getDecoder().decode(session.getSessionKey());
        byte[] encryptedData = Base64.getDecoder().decode(wxEncryptedData.getEncryptedData());
        byte[] iv = Base64.getDecoder().decode(wxEncryptedData.getIv());

        generalAesCipher.init(sessionKey, iv);
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
}
