package com.davidgjm.oss.wechat.model;

public enum SimpleCacheKey {
    EVCS_TOKEN("EvcsToken"),
    WX_ACCESS_TOKEN("wxAccessToken"),

    ;
    private String key;

    SimpleCacheKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
