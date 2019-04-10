package com.davidgjm.oss.wechat.templatemessage;

import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessageConfig;
import com.davidgjm.oss.wechat.templatemessage.WxTemplateNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface WxTemplateMessageConfigService {


    /**
     * Find the Wx Template message configuration with the provided template name
     * @param templateName The template name to look for
     * @return A message config entry
     * @throws WxTemplateNotFoundException if the template message configuration cannot be found.
     */
    WxTemplateMessageConfig findByName(@NotBlank @NotNull String templateName) throws WxTemplateNotFoundException;
}
