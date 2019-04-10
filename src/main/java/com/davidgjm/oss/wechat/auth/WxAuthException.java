package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.GenericWxException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "WeChat Auth failed.")
public class WxAuthException extends GenericWxException {
    public WxAuthException() {
    }

    public WxAuthException(String s) {
        super(s);
    }
}
