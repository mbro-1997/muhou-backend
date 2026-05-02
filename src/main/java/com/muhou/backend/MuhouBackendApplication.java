package com.muhou.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.muhou.backend.infrastructure.persistence.mapper")
@ConfigurationPropertiesScan
@EnableScheduling
@SpringBootApplication
public class MuhouBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuhouBackendApplication.class, args);
    }
}
