package com.davidgjm.oss.wechat.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class WebConfig extends DelegatingWebMvcConfiguration {
    @Configuration
    public static class UnconditionalWebMvcAutoConfiguration extends WebMvcAutoConfiguration {//forces @EnableWebMvc
    }

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new PathTweakingRequestMappingHandlerMapping();
    }

    @Bean
    @Primary
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return super.requestMappingHandlerMapping();
    }
}
