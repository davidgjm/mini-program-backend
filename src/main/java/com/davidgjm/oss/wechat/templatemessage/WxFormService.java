package com.davidgjm.oss.wechat.templatemessage;

import com.davidgjm.oss.wechat.templatemessage.WxForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface WxFormService {

    WxForm addFormid(@NotNull @NotBlank String openid, @NotNull @NotBlank String formid);


    void markAsUsed(@NotNull @NotBlank String openid, @NotNull @NotBlank String formid);

    void remove(@NotNull @NotBlank String openid, @NotNull @NotBlank String formid);

    /**
     * Get a usable formid for the provided openid.
     * @param openid The openid for the user with formids
     * @return Optional may or may not contain the formid
     */
    Optional<String> getAnyAvailableFormid(@NotNull @NotBlank String openid);
}
