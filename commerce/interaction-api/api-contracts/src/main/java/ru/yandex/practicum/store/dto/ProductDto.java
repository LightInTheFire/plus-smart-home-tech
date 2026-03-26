package ru.yandex.practicum.store.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductDto(UUID productId,

    @NotBlank String productName,

    @NotBlank String description,

    @NotBlank String imageSrc,

    @NotNull QuantityState quantityState,

    @NotNull ProductState productState,

    @NotNull ProductCategory productCategory,

    @Positive @NotNull BigDecimal price) {}
