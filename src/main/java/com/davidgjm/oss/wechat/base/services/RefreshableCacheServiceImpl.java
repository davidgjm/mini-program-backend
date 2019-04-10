package com.davidgjm.oss.wechat.base.services;

import com.davidgjm.oss.wechat.base.SimpleCacheProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@Service
public class RefreshableCacheServiceImpl implements RefreshableCacheService {
    private final SimpleCacheProvider cacheProvider;

    public RefreshableCacheServiceImpl(SimpleCacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    @Override
    public <T> T getCachedItem(String key, Class<T> valueType, Predicate<T> refreshCondition, Supplier<T> refreshFunction) {
        T cachedValue = cacheProvider.getCache(key, valueType);
        if (cachedValue != null) {
            log.info("{} found in cache.", valueType.getSimpleName());
            if (refreshCondition.test(cachedValue)) {
                log.warn("{} in the cache needs to be refreshed! Processing.", key);
                cachedValue = refreshFunction.get();
                cacheProvider.putCache(key, cachedValue);
            }
            return cachedValue;
        } else {
            log.info("{} not found in cache. Saving into cache", valueType.getSimpleName());
            T cachedItem = refreshFunction.get();
            cacheProvider.putCache(key, cachedItem);
            return cachedItem;
        }
    }
}
