package com.davidgjm.oss.wechat.wxuserinfo;

import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptedUserInfoDTO extends WxEncryptedData {
    private String rawData;
    private String signature;

}
