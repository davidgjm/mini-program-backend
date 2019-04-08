package com.davidgjm.oss.wechat.dto;

import com.davidgjm.oss.wechat.model.WxEncryptedData;
import lombok.Data;

@Data
public class WxLoginDTO extends WxEncryptedData {
    private String rawData;
    private String signature;


}
