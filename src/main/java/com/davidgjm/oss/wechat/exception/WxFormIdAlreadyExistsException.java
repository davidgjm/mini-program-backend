package com.davidgjm.oss.wechat.exception;

public class WxFormIdAlreadyExistsException extends WxTemplateMessageException {
    public WxFormIdAlreadyExistsException() {
    }

    public WxFormIdAlreadyExistsException(String message) {
        super(message);
    }
}
