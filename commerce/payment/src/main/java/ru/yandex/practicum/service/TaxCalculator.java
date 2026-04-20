package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.yandex.practicum.config.AppProperties;

import org.springframework.stereotype.Component;

@Component
public class TaxCalculator {

    private final BigDecimal taxRate;

    public TaxCalculator(AppProperties appProperties) {
        this.taxRate = appProperties.taxRatePercent()
            .divide(new BigDecimal(100), RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(taxRate)
            .setScale(2, RoundingMode.HALF_UP);
    }
}
