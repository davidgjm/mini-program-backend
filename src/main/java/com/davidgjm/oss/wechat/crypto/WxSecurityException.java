package com.davidgjm.oss.wechat.crypto;

import com.davidgjm.oss.wechat.base.GenericWxException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WxSecurityException extends GenericWxException {
    public WxSecurityException() {
        super();
    }

    public WxSecurityException(String message) {
        super(message);
    }

    public WxSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxSecurityException(Throwable cause) {
        super(cause);
    }

    public WxSecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
