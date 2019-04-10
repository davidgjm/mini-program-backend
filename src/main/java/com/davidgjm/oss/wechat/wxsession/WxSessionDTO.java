package com.davidgjm.oss.wechat.wxsession;

import com.davidgjm.oss.wechat.auth.WxSkeyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxSessionDTO extends WxSkeyDTO {

    private boolean isValidUser;
}
