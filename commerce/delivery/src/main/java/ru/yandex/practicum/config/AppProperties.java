package ru.yandex.practicum.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("delivery")
public record AppProperties(BigDecimal baseRate) {}
