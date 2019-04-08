package com.davidgjm.oss.wechat.wxapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.davidgjm.oss.wechat.domain.WxSession;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxApiSessionResponse extends AbstractWxApiResponse {
    private String openid;
    @JsonProperty("session_key")
    private String sessionKey;
    private String unionid;

    public WxSession toWxSession() {
        WxSession session = new WxSession();
        session.setOpenid(this.openid);
        session.setSessionKey(this.sessionKey);
        return session;
    }
}
