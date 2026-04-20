package ru.yandex.practicum.delivery.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("delivery")
public record AppProperties(BigDecimal baseRate) {}
