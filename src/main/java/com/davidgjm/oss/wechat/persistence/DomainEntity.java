package com.davidgjm.oss.wechat.persistence;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public interface DomainEntity extends Serializable, Persistable<Long> {
}
