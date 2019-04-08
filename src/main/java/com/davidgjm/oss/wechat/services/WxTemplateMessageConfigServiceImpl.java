package com.davidgjm.oss.wechat.services;

import com.davidgjm.oss.wechat.domain.WxMessageConfig;
import com.davidgjm.oss.wechat.exception.WxTemplateNotFoundException;
import com.davidgjm.oss.wechat.repositories.WxMessageConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Slf4j
@Service
public class WxTemplateMessageConfigServiceImpl implements WxTemplateMessageConfigService {
    private final WxMessageConfigRepository messageConfigRepository;

    public WxTemplateMessageConfigServiceImpl(WxMessageConfigRepository messageConfigRepository) {
        this.messageConfigRepository = messageConfigRepository;
    }

    @Override
    public WxMessageConfig findByName(@NotBlank @NotNull String templateName) {
        log.debug("Finding Template Message {}", templateName);
        return messageConfigRepository.findByName(templateName).orElseThrow(WxTemplateNotFoundException::new);
    }
}
