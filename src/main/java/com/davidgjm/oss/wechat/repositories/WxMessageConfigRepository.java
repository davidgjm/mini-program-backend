package com.davidgjm.oss.wechat.repositories;

import com.davidgjm.oss.wechat.domain.WxMessageConfig;

import java.util.Optional;

public interface WxMessageConfigRepository extends GenericRepository<WxMessageConfig> {

    Optional<WxMessageConfig> findByTemplateId(String templateId);


    Optional<WxMessageConfig> findByName(String name);
}
