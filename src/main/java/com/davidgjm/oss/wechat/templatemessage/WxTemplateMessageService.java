package com.davidgjm.oss.wechat.templatemessage;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

public interface WxTemplateMessageService {

    /**
     * Sends a template message request to Tencent WeChat API
     * @param templateName The message template name
     * @param recipientOpenid The end user's openid
     * @param messageData The data to be sent
     * @param messageConsumer The consumer function for handling the template message before sending out.
     * @return The response from WeChat API
     */
    WxApiTemplateMessageResponse sendTemplateMessage(@NotNull @NotBlank String templateName,
                                                     @NotNull @NotBlank String recipientOpenid,
                                                     @NotNull @Valid WxTemplateMessageData messageData,
                                                     @NotNull Consumer<WxTemplateMessage> messageConsumer,
                                                     @NotNull Consumer<WxApiTemplateMessageResponse> responseConsumer);


    default WxApiTemplateMessageResponse sendTemplateMessage(@NotNull @NotBlank String templateName,
                                                     @NotNull @NotBlank String recipientOpenid,
                                                     @NotNull @Valid WxTemplateMessageData messageData,
                                                     @NotNull Consumer<WxApiTemplateMessageResponse> responseConsumer){
        return sendTemplateMessage(templateName, recipientOpenid, messageData, message -> {}, responseConsumer);
    }

    default WxApiTemplateMessageResponse sendTemplateMessage(@NotNull @NotBlank String templateName,
                                                     @NotNull @NotBlank String recipientOpenid,
                                                     @NotNull @Valid WxTemplateMessageData messageData){
        return sendTemplateMessage(templateName, recipientOpenid, messageData, message -> {}, response -> {});
    }

}
