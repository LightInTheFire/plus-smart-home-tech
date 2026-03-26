package ru.yandex.practicum.warehouse.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NewProductInWarehouseRequest(@NotNull UUID productId,

    Boolean fragile,

    @NotNull DimensionDto dimension,

    @Positive @NotNull Double weight) {}
