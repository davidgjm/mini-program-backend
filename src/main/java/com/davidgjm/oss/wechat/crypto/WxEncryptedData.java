package com.davidgjm.oss.wechat.crypto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxEncryptedData {
    @NotBlank
    private String encryptedData;
    private String iv;
    private String rawData;
    private String signature;
}
