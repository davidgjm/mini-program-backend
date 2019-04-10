package com.davidgjm.oss.wechat.wxuser;

import org.springframework.stereotype.Component;

@Component
public class WxUserMapper {
    public WxUserInfoDTO toWxUserInfoDto(WxUser wxUser) {
        if ( wxUser == null ) {
            return null;
        }

        WxUserInfoDTO wxUserInfoDTO = new WxUserInfoDTO();

        return wxUserInfoDTO;
    }

    public WxUser toWxUser(WxUserInfoDTO userInfoDTO) {
        if ( userInfoDTO == null ) {
            return null;
        }

        WxUser wxUser = new WxUser();

        return wxUser;
    }
}
