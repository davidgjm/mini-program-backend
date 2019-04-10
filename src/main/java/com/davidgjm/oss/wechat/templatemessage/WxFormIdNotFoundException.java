package com.davidgjm.oss.wechat.templatemessage;

import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessageException;

public class WxFormIdNotFoundException extends WxTemplateMessageException {
    public WxFormIdNotFoundException() {
    }

    public WxFormIdNotFoundException(String message) {
        super(message);
    }
}
