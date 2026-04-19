package ru.yandex.practicum.order.dto;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductReturnRequest(@NotNull UUID orderId, @NotEmpty Map<UUID, Long> products) {}
