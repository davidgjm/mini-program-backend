package com.davidgjm.oss.wechat.wxuser;

import com.davidgjm.oss.wechat.base.GenericRepository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserRepository extends GenericRepository<WxUser> {
    Optional<WxUser> findByOpenid(String openid);

    Optional<WxUser> findByPhoneNumber(String phoneNumber);


    boolean existsByOpenidAndPhoneNumberNotNull(@NotNull @NotBlank String openid);
}
