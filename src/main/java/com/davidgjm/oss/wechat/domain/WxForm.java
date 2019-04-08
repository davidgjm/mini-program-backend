package com.davidgjm.oss.wechat.domain;

import com.davidgjm.oss.wechat.persistence.GenericEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "idx_uni_form_id", columnNames = {"openid", "formid"})
        },
        indexes = {
                @Index(name = "idx_form_id", columnList = "formid"),
                @Index(name = "idx_open_id", columnList = "openid")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WxForm extends GenericEntity {
    @NotNull
    @NotBlank
    @Column(nullable = false, updatable = false)
    private String openid;

    @NotNull
    @NotBlank
    @Column(nullable = false, updatable = false)
    private String formid;

    @Column(nullable = false)
    private boolean isUsed;
}
