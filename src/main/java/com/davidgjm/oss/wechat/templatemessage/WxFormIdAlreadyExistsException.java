package com.davidgjm.oss.wechat.templatemessage;

public class WxFormIdAlreadyExistsException extends WxTemplateMessageException {
    public WxFormIdAlreadyExistsException() {
    }

    public WxFormIdAlreadyExistsException(String message) {
        super(message);
    }
}
