package ru.yandex.practicum.delivery.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.warehouse.dto.AddressDto;

public record DeliveryDto(@NotNull UUID deliveryId, @NotNull AddressDto fromAddress, @NotNull AddressDto toAddress,
    @NotNull UUID orderId, DeliveryState deliveryState) {}
