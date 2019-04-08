package com.davidgjm.oss.wechat.domain;

import com.davidgjm.oss.wechat.persistence.Auditable;
import com.davidgjm.oss.wechat.persistence.GenericEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class WxMessageConfig extends Auditable {

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
