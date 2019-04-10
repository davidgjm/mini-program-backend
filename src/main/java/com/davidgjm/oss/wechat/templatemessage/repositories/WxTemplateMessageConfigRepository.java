package com.davidgjm.oss.wechat.templatemessage.repositories;

import com.davidgjm.oss.wechat.templatemessage.domain.WxTemplateMessageConfig;
import com.davidgjm.oss.wechat.base.GenericRepository;

import java.util.Optional;

public interface WxTemplateMessageConfigRepository extends GenericRepository<WxTemplateMessageConfig> {

    Optional<WxTemplateMessageConfig> findByTemplateId(String templateId);


    Optional<WxTemplateMessageConfig> findByName(String name);
}
