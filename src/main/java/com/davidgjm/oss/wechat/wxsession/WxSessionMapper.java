package com.davidgjm.oss.wechat.wxsession;

import org.springframework.stereotype.Component;

@Component
public class WxSessionMapper {
    public WxSessionDTO wxSessionToWxSessionDTO(WxSession session) {
        if ( session == null ) {
            return null;
        }

        WxSessionDTO wxSessionDTO = new WxSessionDTO();

        wxSessionDTO.setSkey( session.getSkey() );

        return wxSessionDTO;
    }
}