package com.davidgjm.oss.wechat.base.services;

import java.util.function.Predicate;
import java.util.function.Supplier;

@FunctionalInterface
public interface RefreshableCacheService {

    <T extends Object> T getCachedItem(String key, Class<T> valueTpe, Predicate<T> refreshCondition, Supplier<T> refreshFunction);
}
