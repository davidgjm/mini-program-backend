package com.davidgjm.oss.wechat.wxsession;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface WxSessionService {

    /**
     * Requests new WXSession with the provided login code from Tencent WeChat server.
     * @param loginCode The login token
     * @return Session information returned by Tencent WeChat Server.
     */
    WxSession requestNewSession(@NotNull @NotBlank String loginCode);

    WxSessionDTO createOrUpdate(WxSession session);

    Optional<WxSession> findByLoginCode(String loginCode);

    Optional<WxSession> findByOpenid(String openid);

    WxSession findBySkey(String skey);

    void deleteByOpenid(@NotNull @NotBlank String openid);

    WxSessionDTO wxSessionToWxSessionDTO(WxSession session);
}
