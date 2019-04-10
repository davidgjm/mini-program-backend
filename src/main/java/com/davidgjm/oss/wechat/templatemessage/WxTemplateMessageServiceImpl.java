package com.davidgjm.oss.wechat.templatemessage;

import com.davidgjm.oss.wechat.base.services.WxApiService;
import com.davidgjm.oss.wechat.wxuser.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class WxTemplateMessageServiceImpl implements WxTemplateMessageService {
    private final WxApiService apiService;
    private final WxFormService wxFormService;
    private final WxUserService wxUserService;

    private WxTemplateMessageConfigService templateMessageConfigService;


    public WxTemplateMessageServiceImpl(WxApiService apiService, WxFormService wxFormService, WxUserService wxUserService) {
        this.apiService = apiService;
        this.wxFormService = wxFormService;
        this.wxUserService = wxUserService;
    }

    @Autowired
    public void setTemplateMessageConfigService(WxTemplateMessageConfigService templateMessageConfigService) {
        this.templateMessageConfigService = templateMessageConfigService;
    }

    @Override
    public WxApiTemplateMessageResponse sendTemplateMessage(String templateName, String recipientOpenid, WxTemplateMessageData messageData,
                                                            Consumer<WxTemplateMessage> messageConsumer,
                                                            Consumer<WxApiTemplateMessageResponse> responseConsumer) {
        log.debug("Sending template message {} to user {}", templateName, recipientOpenid);

        validateUser(recipientOpenid);

        WxTemplateMessage templateMessage = buildMessage(templateName, recipientOpenid, messageData);
        log.debug("templateMessage is {}",templateMessage);
        //if form id is mocked, return empty
        if (templateMessage.getFormId() == null || templateMessage.getFormId().contains("mock one")) {
            String msg = "The form id is missing or mocked. " + templateMessage.getFormId();
            log.warn(msg);
            WxApiTemplateMessageResponse response = new WxApiTemplateMessageResponse();
            response.setTemplateId(templateMessage.getTemplateId());
            response.setErrcode(400);
            response.setErrmsg(msg);
            return response;
        }

        if (messageConsumer != null) {
            log.debug("Processing template message consumer...");
            messageConsumer.accept(templateMessage);
        }
        WxApiTemplateMessageResponse response = apiService.sendTemplateMessage(templateMessage);
        if (responseConsumer != null) {
            responseConsumer.accept(response);
        }
        return response;
    }


    private void validateUser(String openid) {
        log.debug("Validating user openid {}", openid);
        wxUserService.isRegisteredUser(openid);
    }

    private WxTemplateMessage buildMessage(String templateName, String recipientOpenid, WxTemplateMessageData messageData) {
        log.debug("Building template message for {}", templateName);
        WxTemplateMessageConfig templateMessageConfig = templateMessageConfigService.findByName(templateName);

        log.debug("configEntry's page is: {}",templateMessageConfig.getPage());
        WxTemplateMessage.WxTemplateMessageBuilder builder = WxTemplateMessage.builder()
                .recipientOpenid(recipientOpenid)
                .templateId(templateMessageConfig.getTemplateId())
                .formId(getFormId(recipientOpenid))
                .emphasisKeyword(templateMessageConfig.getEmphasisKeyword())
                .data(messageData.getData());
        if (!templateMessageConfig.getPage().isEmpty()) builder.redirectPage(templateMessageConfig.getPage());
        return builder.build();
    }

    private String getFormId(String openid) {
        return wxFormService.getAnyAvailableFormid(openid).orElse("the formId is a mock one");
    }
}
