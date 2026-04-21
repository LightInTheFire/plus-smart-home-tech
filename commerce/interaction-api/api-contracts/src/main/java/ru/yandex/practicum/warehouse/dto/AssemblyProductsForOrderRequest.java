package ru.yandex.practicum.warehouse.dto;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AssemblyProductsForOrderRequest(@NotNull UUID orderId, @NotEmpty Map<UUID, Long> products) {}
