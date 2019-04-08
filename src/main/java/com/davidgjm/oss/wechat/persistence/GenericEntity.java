package com.davidgjm.oss.wechat.persistence;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GenericEntity extends AbstractPersistable<Long> implements DomainEntity {

    public void setId(Long id) {
        super.setId(id);
    }
}
