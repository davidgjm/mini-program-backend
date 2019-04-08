package com.davidgjm.oss.wechat;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
public class SimpleCacheProvider {
    private final ConcurrentHashMap<String, Object> cache;

    public SimpleCacheProvider() {
        this.cache = new ConcurrentHashMap<>(4);
    }

    public <T extends Object> T getCache(String key, Class<T> valueType) {
        Object result=cache.get(key);
        if (result == null) return null;
        if (result.getClass().equals(valueType)) {
            return (T) result;
        } else {
            throw new IllegalArgumentException("The stored value is not of type "+valueType.getName());
        }
    }

    public <T extends Object> void putCache(String key, T value) {
        cache.put(key, value);
    }
}
