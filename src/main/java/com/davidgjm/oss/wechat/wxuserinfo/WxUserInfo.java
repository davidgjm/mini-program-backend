package com.davidgjm.oss.wechat.wxuserinfo;

import com.davidgjm.oss.wechat.base.persistence.GenericEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
public class WxUserInfo extends GenericEntity {

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true, updatable = false, length = 128)
    private String openId;
    private String nickName;
    private Integer gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private String language;

    @Column(nullable = false)
    private boolean isActive;


    @Column(unique = true)
    private String phoneNumber;


    @Column(nullable = false, updatable = false)
    protected LocalDateTime creationDate;

    @PrePersist
    private void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}
