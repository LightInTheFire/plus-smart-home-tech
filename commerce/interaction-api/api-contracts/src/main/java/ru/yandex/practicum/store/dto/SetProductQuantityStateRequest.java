package ru.yandex.practicum.store.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record SetProductQuantityStateRequest(@NotNull UUID productId, @NotNull QuantityState quantityState) {}
