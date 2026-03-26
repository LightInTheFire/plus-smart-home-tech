package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DimensionDto(@Positive @NotNull Double width,

    @Positive @NotNull Double height,

    @Positive @NotNull Double depth) {}
