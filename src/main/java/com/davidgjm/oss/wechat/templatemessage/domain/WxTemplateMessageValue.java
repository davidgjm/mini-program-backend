package com.davidgjm.oss.wechat.templatemessage.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class WxTemplateMessageValue {
    @NotNull
    private String value;
}
