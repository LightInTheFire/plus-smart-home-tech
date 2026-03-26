package ru.yandex.practicum.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;

import ru.yandex.practicum.store.dto.ProductCategory;
import ru.yandex.practicum.store.dto.ProductState;
import ru.yandex.practicum.store.dto.QuantityState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Column(name = "image_src", nullable = false, length = 512)
    private String imageSrc;

    @Column(name = "quantity", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private QuantityState quantity;

    @Column(name = "state", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ProductState state;

    @Column(name = "category", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

}
