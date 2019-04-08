package com.davidgjm.oss.wechat.exception;

public class WxTemplateNotFoundException extends WxTemplateMessageException {
    public WxTemplateNotFoundException() {
    }

    public WxTemplateNotFoundException(String message) {
        super(message);
    }

    public WxTemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxTemplateNotFoundException(Throwable cause) {
        super(cause);
    }

    public WxTemplateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
