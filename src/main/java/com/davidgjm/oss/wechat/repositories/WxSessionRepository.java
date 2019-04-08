package com.davidgjm.oss.wechat.repositories;

import com.davidgjm.oss.wechat.domain.WxSession;

import java.util.Optional;

public interface WxSessionRepository extends GenericRepository<WxSession> {
    Optional<WxSession> findByCode(String code);

    Optional<WxSession> findByOpenid(String openid);

    Optional<WxSession> findBySkey(String skey);
}
