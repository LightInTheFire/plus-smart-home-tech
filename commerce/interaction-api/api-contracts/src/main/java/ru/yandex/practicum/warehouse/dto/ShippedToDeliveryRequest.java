package ru.yandex.practicum.warehouse.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ShippedToDeliveryRequest(@NotNull UUID orderId, @NotNull UUID deliveryId) {}
