package com.davidgjm.oss.wechat.crypto;

import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import com.davidgjm.oss.wechat.auth.WxLoginDTO;
import com.davidgjm.oss.wechat.wxuser.WxUserInfoDTO;
import com.davidgjm.oss.wechat.wxsession.WxSession;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

public interface WxCryptoService {

    String decryptPhoneNumber(@NotNull @Validated WxSession session, WxEncryptedData encryptedData);


    WxUserInfoDTO decryptUserInfo(@NotNull @Validated WxSession session, WxLoginDTO wxLoginDTO);

    void encryptSessionKey(@NotNull @Validated WxSession session);
}
