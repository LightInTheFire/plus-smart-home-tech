package ru.yandex.practicum.cart.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ShoppingCartDto(@NotNull UUID shoppingCartId,

    @NotEmpty @Valid List<Long> products) {}
