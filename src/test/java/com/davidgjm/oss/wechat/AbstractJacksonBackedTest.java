package com.davidgjm.oss.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;

public abstract class AbstractJacksonBackedTest {
    protected static final ObjectMapper objectMapper= new ObjectMapper();

    public AbstractJacksonBackedTest() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
    }
}
