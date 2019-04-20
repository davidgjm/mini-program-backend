package com.davidgjm.oss.wechat.crypto;

import com.davidgjm.oss.wechat.wxsession.WxSession;
import com.davidgjm.oss.wechat.wxuserinfo.WxUserInfoDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface WxCryptoService {

    String decryptPhoneNumber(@NotBlank String sessionKey, WxEncryptedData encryptedData);


    WxUserInfoDTO decryptUserInfo(@NotBlank String sessionKey, WxEncryptedData encryptedData);

    void encryptSessionKey(@NotNull @Validated WxSession session);

    void validateSignature(@NotBlank String sessionKey, String providedSignature, String rawData);
}
