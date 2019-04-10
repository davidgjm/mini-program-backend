package com.davidgjm.oss.wechat.templatemessage;

import com.davidgjm.oss.wechat.base.GenericWxException;

public class WxTemplateMessageException extends GenericWxException {
    public WxTemplateMessageException() {
    }

    public WxTemplateMessageException(String message) {
        super(message);
    }

    public WxTemplateMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxTemplateMessageException(Throwable cause) {
        super(cause);
    }

    public WxTemplateMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
