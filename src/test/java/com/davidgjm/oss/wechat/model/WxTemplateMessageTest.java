package com.davidgjm.oss.wechat.model;

import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessage;
import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessageData;
import com.davidgjm.oss.wechat.templatemessage.WxTemplateMessageValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.davidgjm.oss.wechat.AbstractJacksonBackedTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WxTemplateMessageTest extends AbstractJacksonBackedTest {
    private WxTemplateMessage message;
    private WxTemplateMessageData keywordValues = new WxTemplateMessageData();

    @Before
    public void setUp() throws Exception {
        keywordValues.addField("keyword1", new WxTemplateMessageValue("This is order number"));
        keywordValues.addField("keyword2", new WxTemplateMessageValue("This is booking time, from/to"));
        keywordValues.addField("keyword3", new WxTemplateMessageValue("This is POI address"));
        keywordValues.addField("keyword4", new WxTemplateMessageValue("This is site guide"));
        keywordValues.addField("keyword5", new WxTemplateMessageValue("This is reminder"));
        keywordValues.addField("keyword6", new WxTemplateMessageValue("This is note"));

    }

    @Test
    public void testToJson() throws JsonProcessingException {
        message = WxTemplateMessage.builder()
                .recipientOpenid("openid here")
                .templateId("8E0OsCwvxSH8YlM1fzfjVZU8cw0JqJKFIFLRm4amjFQ")
                .redirectPage("/pages/index")
                .formId("formid#123344555")
                .data(keywordValues.getData())
                .emphasisKeyword("keyword2")
                .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        assertNotNull(json);
        System.out.println(json);
    }

    @Test
    public void testToJsonWithOptionalFieldsMissing() throws JsonProcessingException {
        message = WxTemplateMessage.builder()
                .recipientOpenid("openid here")
                .templateId("8E0OsCwvxSH8YlM1fzfjVZU8cw0JqJKFIFLRm4amjFQ")
                .formId("formid#123344555")
                .data(keywordValues.getData())
                .emphasisKeyword("keyword2")
                .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        assertNotNull(json);
        System.out.println(json);
    }
    @Test
    public void testToJso_empty_values() throws JsonProcessingException {
        message = WxTemplateMessage.builder()
                .recipientOpenid("openid here")
                .templateId("8E0OsCwvxSH8YlM1fzfjVZU8cw0JqJKFIFLRm4amjFQ")
                .formId("formid#123344555")
                .redirectPage("")
                .data(keywordValues.getData())
                .emphasisKeyword("keyword2")
                .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message);
        assertNotNull(json);
        System.out.println(json);
    }
}