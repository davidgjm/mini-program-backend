package com.davidgjm.oss.wechat.config;

import com.davidgjm.oss.wechat.persistence.AuditorAwareImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.security.Security;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ApplicationSpringConfig {

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());

    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
}
