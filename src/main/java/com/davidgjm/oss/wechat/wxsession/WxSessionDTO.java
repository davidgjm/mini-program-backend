package com.davidgjm.oss.wechat.wxsession;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WxSessionDTO {
    @NotBlank
    private String skey;
}
