package com.davidgjm.oss.wechat.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractWxApiResponse implements Serializable {
    private Integer errcode;
    private String errmsg;
}
