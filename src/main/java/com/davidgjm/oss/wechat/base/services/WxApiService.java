package com.davidgjm.oss.wechat.base.services;

import com.davidgjm.oss.wechat.auth.WxApiAccessTokenResponse;
import com.davidgjm.oss.wechat.wxsession.WxApiSessionResponse;
import com.davidgjm.oss.wechat.templatemessage.WxApiTemplateMessageResponse;
import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface WxApiService {

    WxApiSessionResponse getSession(String loginCode);

    WxApiAccessTokenResponse getAccessToken();

    WxApiTemplateMessageResponse sendTemplateMessage(@NotNull @Valid WxTemplateMessage templateMessage);
}
