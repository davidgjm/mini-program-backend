package com.davidgjm.oss.wechat.services;

import com.davidgjm.oss.wechat.domain.WxMessageConfig;
import com.davidgjm.oss.wechat.exception.WxTemplateNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface WxTemplateMessageConfigService {

    WxMessageConfig findByName(@NotBlank @NotNull String templateName) throws WxTemplateNotFoundException;
}
