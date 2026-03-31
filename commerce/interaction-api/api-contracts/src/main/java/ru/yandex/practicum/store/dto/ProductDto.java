package ru.yandex.practicum.store.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductDto(@JsonProperty("productId") UUID id,

    @JsonProperty("productName") @NotBlank String name,

    @NotBlank String description,

    @NotBlank String imageSrc,

    @JsonProperty("quantityState") @NotNull QuantityState quantity,

    @JsonProperty("productState") @NotNull ProductState state,

    @JsonProperty("productCategory") @NotNull ProductCategory category,

    @Positive @NotNull BigDecimal price) {}
