package ru.yandex.practicum.cart.dto;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ShoppingCartDto(@NotNull UUID shoppingCartId, @NotEmpty Map<UUID, Long> products) {

    public static ShoppingCartDto getEmpty() {
        return new ShoppingCartDto(null, Map.of());
    }
}
