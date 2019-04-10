package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.crypto.WxEncryptedData;
import lombok.Data;

@Data
public class WxLoginDTO extends WxEncryptedData {
    private String rawData;
    private String signature;


}
