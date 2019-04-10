package com.davidgjm.oss.wechat.templatemessage.repositories;

import com.davidgjm.oss.wechat.base.GenericRepository;
import com.davidgjm.oss.wechat.templatemessage.domain.WxForm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface WxFormRepository extends GenericRepository<WxForm> {
    List<WxForm> findAllByOpenid(@NotNull @NotBlank String openid);

    List<WxForm> findAllByOpenidAndIsUsedFalse(@NotNull @NotBlank String openid);

    Optional<WxForm> findByOpenidAndFormid(@NotNull @NotBlank String openid, @NotNull @NotBlank String formid);
}
