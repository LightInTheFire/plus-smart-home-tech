package ru.yandex.practicum.cart.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChangeProductQuantityRequest(@NotNull UUID productId,

    @Positive long newQuantity) {}
