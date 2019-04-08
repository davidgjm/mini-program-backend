package com.davidgjm.oss.wechat.util;

import com.davidgjm.oss.wechat.persistence.DomainEntity;
import org.springframework.util.Assert;

public final class ParamChecker {
    private ParamChecker() {
    }

    public static void checkArg(String param, String message) {
        Assert.hasText(param, () -> {
            if (message == null) {
                return "Parameter cannot be null or empty!";
            } else {
                return message;
            }
        });
    }

    public static void checkArg(String param) {
        checkArg(param, (String) null);
    }

    public static <T extends DomainEntity> void checkId(T entity) {
        Assert.notNull(entity.getId(),"ID is required!");
    }
}
