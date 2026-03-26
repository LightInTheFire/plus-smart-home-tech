package ru.yandex.practicum.warehouse.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddProductToWarehouseRequest(@NotNull UUID productId,

    @Positive @NotNull Long quantity) {}
