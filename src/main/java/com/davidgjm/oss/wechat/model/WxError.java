package com.davidgjm.oss.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WxError {
    private String errcode;
    private String errmsg;

}
