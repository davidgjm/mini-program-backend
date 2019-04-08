package com.davidgjm.oss.wechat.services;

import com.davidgjm.oss.wechat.exception.WxUserNotFoundException;
import com.davidgjm.oss.wechat.domain.WxUser;
import com.davidgjm.oss.wechat.dto.WxLoginDTO;
import com.davidgjm.oss.wechat.dto.WxSessionDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface WxUserManagementService {


    WxSessionDTO startSession(String loginCode);

    WxSessionDTO checkSession(String skey);

    boolean validateSignature(WxLoginDTO wxLoginDTO);

    WxUser registerOrLogin(WxLoginDTO wxLoginDTO);

    /**
     * Validates if the provided <code>skey</code> exists and a register wxUser exists with this skey.
     * @param skey The skey to be validated.
     * @return <b>True</b> only if the skey is valid (not expired) and a registered WXUser exists in the system.
     */
    boolean existsUserBySkey(@NotNull @NotBlank String skey);

    /**
     * Check if a session exists with the provided skey.
     * @param skey The skey to be checked.
     * @return <b>True</b> if the skey is valid and a session associated with the provided skey exists.
     */
    boolean existsSessionBySkey(@NotNull @NotBlank String skey);

    /**
     * Finds the WxUser associated with the provided skey.
     * @param skey The skey to be queried.
     * @return WxUser
     * @throws WxUserNotFoundException if the user is not found or the skey is invalid.
     */
    WxUser findUserBySkey(@NotNull @NotBlank String skey) throws WxUserNotFoundException;
}
