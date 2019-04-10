package com.davidgjm.oss.wechat.templatemessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class WxTemplateMessage {

    @JsonProperty(value = "touser", required = true)
    private String recipientOpenid;

    @JsonProperty(value = "template_id", required = true)
    private String templateId;

    @JsonProperty(value = "page")
    private String redirectPage;

    @JsonProperty(value = "form_id", required = true)
    private String formId;

    private Map<String, WxTemplateMessageValue> data;

    @JsonProperty("emphasis_keyword")
    private String emphasisKeyword;


}
