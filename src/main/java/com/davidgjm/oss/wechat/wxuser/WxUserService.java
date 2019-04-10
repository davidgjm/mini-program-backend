package com.davidgjm.oss.wechat.wxuser;

import com.davidgjm.oss.wechat.wxuser.WxUserInfoDTO;
import com.davidgjm.oss.wechat.wxuser.WxUser;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

public interface WxUserService {
    Optional<WxUser> findByOpenid(String openid);

    WxUser saveOrUpdate(WxUser user);

    WxUser saveOrUpdate(WxUserInfoDTO userInfoDTO);

    Optional<WxUser> findUserByPhone(@NotNull @NotBlank @Pattern(regexp = "\\d{11}") String phoneNumber);

    void deleteUser(@NotNull @Validated WxUser wxUser);

    void deleteUserByPhoneNumber(@NotNull @NotBlank @Pattern(regexp = "\\d{11}") String phoneNumber);

    /**
     * Check if the user with the provided open id exists and is registered (with phone number)
     * @param openid The openid to check
     * @return <b>True</b> if the user exists and phone number is present.
     */
    boolean isRegisteredUser(@NotNull @NotBlank String openid);
}
