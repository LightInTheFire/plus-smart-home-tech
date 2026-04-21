package ru.yandex.practicum.payment.config;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("payment")
public record AppProperties(BigDecimal taxRatePercent) {}
