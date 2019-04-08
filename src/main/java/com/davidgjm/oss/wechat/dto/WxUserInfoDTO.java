package com.davidgjm.oss.wechat.dto;

import com.davidgjm.oss.wechat.model.WxWatermark;
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
