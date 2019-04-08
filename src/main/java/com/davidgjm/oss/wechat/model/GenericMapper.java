package com.davidgjm.oss.wechat.model;

import com.davidgjm.oss.wechat.dto.GenericDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public interface GenericMapper<D extends GenericDto, E extends Serializable> {
    @NotNull
    D createFrom(@NotNull E entity);

    @NotNull
    E createFrom(@NotNull D dto);
}
