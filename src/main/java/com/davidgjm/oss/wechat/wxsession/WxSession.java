package com.davidgjm.oss.wechat.wxsession;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.davidgjm.oss.wechat.base.persistence.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class WxSession extends Auditable {
    @NotEmpty
    @Column(nullable = false, unique = true, updatable = false, length = 128)
    private String openid;

    private String code;

    @JsonProperty("session_key")
    private String sessionKey;

    private String skey;

    private Integer accessCount;


    @PrePersist
    private void onCreate() {
        this.accessCount = 1;
    }

    @PreUpdate
    private void onUpdate() {
        accessCount++;
    }
}
