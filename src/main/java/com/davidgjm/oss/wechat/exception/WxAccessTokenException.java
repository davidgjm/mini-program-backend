package com.davidgjm.oss.wechat.exception;

import com.davidgjm.oss.wechat.model.WxError;

public class WxAccessTokenException extends GenericWxException{
    private WxError error;

    public WxAccessTokenException() {
    }

    public WxAccessTokenException(String message) {
        super(message);
    }

    public WxAccessTokenException(WxError error) {
        this(String.format("Error getting access token: error code %s, message: %s", error.getErrcode(), error.getErrmsg()));
        this.error = error;
    }

    public WxError getError() {
        return error;
    }
}
