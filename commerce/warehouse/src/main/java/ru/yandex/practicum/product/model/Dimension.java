package ru.yandex.practicum.product.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Dimension {

    @Column(name = "width", nullable = false, precision = 10, scale = 2)
    private BigDecimal width;

    @Column(name = "height", nullable = false, precision = 10, scale = 2)
    private BigDecimal height;

    @Column(name = "depth", nullable = false, precision = 10, scale = 2)
    private BigDecimal depth;

    public BigDecimal calculateVolume() {
        return width.multiply(height)
            .multiply(depth);
    }
}
