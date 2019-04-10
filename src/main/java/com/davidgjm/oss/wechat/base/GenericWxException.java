package com.davidgjm.oss.wechat.base;

public class GenericWxException extends GenericException {
    public GenericWxException() {
    }

    public GenericWxException(String message) {
        super(message);
    }

    public GenericWxException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericWxException(Throwable cause) {
        super(cause);
    }

    public GenericWxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
