package com.davidgjm.oss.wechat.templatemessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Slf4j
@Service
public class WxTemplateMessageConfigServiceImpl implements WxTemplateMessageConfigService {
    private final WxTemplateMessageConfigRepository messageConfigRepository;

    public WxTemplateMessageConfigServiceImpl(WxTemplateMessageConfigRepository messageConfigRepository) {
        this.messageConfigRepository = messageConfigRepository;
    }

    @Override
    public WxTemplateMessageConfig findByName(@NotBlank @NotNull String templateName) {
        log.debug("Finding Template Message {}", templateName);
        return messageConfigRepository.findByName(templateName).orElseThrow(WxTemplateNotFoundException::new);
    }
}
