package com.davidgjm.oss.wechat.wxuserinfo;

import com.davidgjm.oss.wechat.base.GenericRepository;

import java.util.Optional;

public interface WxUserInfoRepository extends GenericRepository<WxUserInfo> {

    Optional<WxUserInfo> findByOpenId(String openid);
}
