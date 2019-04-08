package com.davidgjm.oss.wechat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Wx user does not exist.")
public class WxUserNotFoundException extends GenericWxException {
    public WxUserNotFoundException() {
    }

    public WxUserNotFoundException(String message) {
        super(message);
    }

    public WxUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxUserNotFoundException(Throwable cause) {
        super(cause);
    }

    public WxUserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
