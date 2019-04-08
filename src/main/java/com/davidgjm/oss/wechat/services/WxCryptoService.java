package com.davidgjm.oss.wechat.services;

import com.davidgjm.oss.wechat.model.WxEncryptedData;
import com.davidgjm.oss.wechat.dto.WxLoginDTO;
import com.davidgjm.oss.wechat.dto.WxUserInfoDTO;
import com.davidgjm.oss.wechat.domain.WxSession;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

public interface WxCryptoService {

    String decryptPhoneNumber(@NotNull @Validated WxSession session, WxEncryptedData encryptedData);


    WxUserInfoDTO decryptUserInfo(@NotNull @Validated WxSession session, WxLoginDTO wxLoginDTO);

    void encryptSessionKey(@NotNull @Validated WxSession session);
}
