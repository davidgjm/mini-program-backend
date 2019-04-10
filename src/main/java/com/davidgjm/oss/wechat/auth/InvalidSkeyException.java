package com.davidgjm.oss.wechat.auth;

import com.davidgjm.oss.wechat.base.GenericWxException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid skey")
public class InvalidSkeyException extends GenericWxException {
    public InvalidSkeyException() {
        super();
    }

    public InvalidSkeyException(String message) {
        super(message);
    }

    public InvalidSkeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSkeyException(Throwable cause) {
        super(cause);
    }

    public InvalidSkeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
