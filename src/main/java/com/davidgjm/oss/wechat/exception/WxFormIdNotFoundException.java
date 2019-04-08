package com.davidgjm.oss.wechat.exception;

public class WxFormIdNotFoundException extends WxTemplateMessageException {
    public WxFormIdNotFoundException() {
    }

    public WxFormIdNotFoundException(String message) {
        super(message);
    }
}
