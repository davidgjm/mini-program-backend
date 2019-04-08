package com.davidgjm.oss.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;

@EntityScan(
        basePackageClasses = {MiniProgramBackendApplication.class, Jsr310Converters.class}
)
@SpringBootApplication
public class MiniProgramBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniProgramBackendApplication.class, args);
    }
}
