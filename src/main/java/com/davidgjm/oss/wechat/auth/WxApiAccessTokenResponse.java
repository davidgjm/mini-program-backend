package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.AbstractWxApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WxApiAccessTokenResponse extends AbstractWxApiResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private LocalDateTime expiresBy;
}
