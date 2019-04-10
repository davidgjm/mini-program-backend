package com.davidgjm.oss.wechat.templatemessage.client;

import com.davidgjm.oss.wechat.base.AbstractWxApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxApiTemplateMessageResponse extends AbstractWxApiResponse {
    @JsonProperty("template_id")
    private String templateId;
}
