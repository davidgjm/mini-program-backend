package com.davidgjm.oss.wechat.repositories;

import com.davidgjm.oss.wechat.domain.WxUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface UserRepository extends GenericRepository<WxUser> {
    Optional<WxUser> findByOpenid(String openid);

    Optional<WxUser> findByPhoneNumber(String phoneNumber);


    boolean existsByOpenidAndPhoneNumberNotNull(@NotNull @NotBlank String openid);
}
