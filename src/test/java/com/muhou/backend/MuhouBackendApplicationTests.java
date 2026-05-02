package com.muhou.backend;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = MuhouBackendApplicationTests.TestApplication.class)
class MuhouBackendApplicationTests {

    @SpringBootApplication(scanBasePackages = "com.muhou.backend")
    @MapperScan("com.muhou.backend.infrastructure.persistence.mapper")
    static class TestApplication {
    }

    @Test
    void contextLoads() {
    }
}
