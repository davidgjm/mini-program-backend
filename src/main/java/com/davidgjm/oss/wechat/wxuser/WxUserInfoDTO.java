package com.davidgjm.oss.wechat.wxuser;

import lombok.Data;

@Data
public class WxUserInfoDTO {
    private String openId;
    private String nickName;
    private String avatarUrl;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    private String language;

    private WxWatermark watermark;
}
