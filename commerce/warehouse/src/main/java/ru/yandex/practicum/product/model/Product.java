package ru.yandex.practicum.product.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "fragile", nullable = false)
    private boolean fragile = false;

    @Column(name = "weight", nullable = false, precision = 10, scale = 2)
    private BigDecimal weight;

    @Embedded
    Dimension dimension;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

}
