package com.davidgjm.oss.wechat.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxEncryptedData {
    private String encryptedData;
    private String iv;

    @NotEmpty
    private String skey;
}
