package com.davidgjm.oss.wechat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class WxSkeyDTO {
    @NotNull
    @NotBlank
    private String skey;
}
