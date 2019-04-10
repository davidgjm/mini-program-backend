package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.GenericWxException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "skey not found!")
public class WxSkeyNotFoundException extends GenericWxException {
    public WxSkeyNotFoundException() {
    }

    public WxSkeyNotFoundException(String message) {
        super(message);
    }

    public WxSkeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxSkeyNotFoundException(Throwable cause) {
        super(cause);
    }

    public WxSkeyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
