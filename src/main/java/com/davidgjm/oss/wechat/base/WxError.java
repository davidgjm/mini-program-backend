package com.davidgjm.oss.wechat.base;

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
