package com.davidgjm.oss.wechat.wxuser;

import com.davidgjm.oss.wechat.base.GenericWxException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User phone number not found.")
public class WxUserPhoneNumberNotFoundException extends GenericWxException {
    public WxUserPhoneNumberNotFoundException() {
    }

    public WxUserPhoneNumberNotFoundException(String message) {
        super(message);
    }

    public WxUserPhoneNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WxUserPhoneNumberNotFoundException(Throwable cause) {
        super(cause);
    }

    public WxUserPhoneNumberNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
