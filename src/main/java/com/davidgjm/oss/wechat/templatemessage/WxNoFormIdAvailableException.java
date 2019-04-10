package com.davidgjm.oss.wechat.templatemessage;

public class WxNoFormIdAvailableException extends WxTemplateMessageException {
    public WxNoFormIdAvailableException() {
    }

    public WxNoFormIdAvailableException(String message) {
        super(message);
    }

    public WxNoFormIdAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxNoFormIdAvailableException(Throwable cause) {
        super(cause);
    }

    public WxNoFormIdAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
