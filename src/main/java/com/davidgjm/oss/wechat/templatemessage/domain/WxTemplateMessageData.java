package com.davidgjm.oss.wechat.templatemessage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class WxTemplateMessageData {
    private final Map<String, WxTemplateMessageValue> data=new HashMap<>();

    public Map<String, WxTemplateMessageValue> getData() {
        return data;
    }

    @JsonIgnore
    public WxTemplateMessageData addField(@NotNull @NotBlank String keywordName, @NotNull @Valid WxTemplateMessageValue value) {
        data.put(keywordName, value);
        return this;
    }

    @JsonIgnore
    public WxTemplateMessageData addField(@NotNull @NotBlank String keywordName, @NotNull @NotBlank String value) {
        return addField(keywordName, new WxTemplateMessageValue(value));
    }
}
