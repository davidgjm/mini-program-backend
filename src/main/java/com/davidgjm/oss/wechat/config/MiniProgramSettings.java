package com.davidgjm.oss.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@ConfigurationProperties(prefix = "wx.mini-program")
@Data
public class MiniProgramSettings {
    private String grantType;
    private String authUrl;
}
