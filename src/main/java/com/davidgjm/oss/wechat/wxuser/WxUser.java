package com.davidgjm.oss.wechat.wxuser;

import com.davidgjm.oss.wechat.base.persistence.GenericEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class WxUser extends GenericEntity {

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true, updatable = false, length = 128)
    private String openid;

    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false, updatable = false)
    protected LocalDateTime creationDate;


    private LocalDateTime lastLogin;

    private Integer loginCount;

    @PrePersist
    private void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.lastLogin = this.creationDate;
        this.loginCount = 1;
    }

    @PreUpdate
    private void onUpdate() {
        this.lastLogin = LocalDateTime.now();
    }

    public void incrementLoginCount() {
        if (loginCount == null) {
            loginCount = 0;
        }
        this.loginCount++;
    }
}
