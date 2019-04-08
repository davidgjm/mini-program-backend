package com.davidgjm.oss.wechat.services;

import com.davidgjm.oss.wechat.wxapi.WxApiAccessTokenResponse;
import com.davidgjm.oss.wechat.wxapi.WxApiSessionResponse;
import com.davidgjm.oss.wechat.wxapi.WxApiTemplateMessageResponse;
import com.davidgjm.oss.wechat.model.WxTemplateMessage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WxApiService {

    WxApiSessionResponse getSession(String loginCode);

    WxApiAccessTokenResponse getAccessToken();

    WxApiTemplateMessageResponse sendTemplateMessage(@NotNull @Valid WxTemplateMessage templateMessage);
}
