package ru.yandex.practicum.testcontainers;

import ru.yandex.practicum.AnalyzerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersAnalyzerApp {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:18.3-alpine").withUsername("postgres")
            .withPassword("secret")
            .withDatabaseName("test");
    }

    public static void main(String[] args) {
        SpringApplication.from(AnalyzerApp::main)
            .with(TestcontainersAnalyzerApp.class)
            .run(args);
    }
}
