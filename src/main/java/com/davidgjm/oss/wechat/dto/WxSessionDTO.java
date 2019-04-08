package com.davidgjm.oss.wechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxSessionDTO extends WxSkeyDTO{

    private boolean isValidUser;
}
