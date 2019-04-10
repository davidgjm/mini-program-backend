package com.davidgjm.oss.wechat.wxsession;

import com.davidgjm.oss.wechat.base.GenericRepository;

import java.util.Optional;

public interface WxSessionRepository extends GenericRepository<WxSession> {
    Optional<WxSession> findByCode(String code);

    Optional<WxSession> findByOpenid(String openid);

    Optional<WxSession> findBySkey(String skey);
}
