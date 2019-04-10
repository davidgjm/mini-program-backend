package com.davidgjm.oss.wechat.templatemessage;

import com.davidgjm.oss.wechat.base.persistence.Auditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class WxTemplateMessageConfig extends Auditable {

    @NotEmpty
    @NotNull
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @NotNull
    @Column(nullable = false, unique = true, updatable = false)
    private String templateId;

    private String page;

    private String emphasisKeyword;
}
