package ru.yandex.practicum.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

import ru.yandex.practicum.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.service.ShoppingCartService;
import ru.yandex.practicum.validation.ValidUsername;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @ValidUsername String username) {
        log.info("Getting shopping cart for user: {}", username);
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam @ValidUsername String username,
        @RequestBody @NotEmpty Map<UUID, Long> products) {
        log.info("Adding products to cart for user: {}, products: {}", username, products);
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    @DeleteMapping
    public void deactivateCurrentShoppingCart(@RequestParam @ValidUsername String username) {
        log.info("Deactivating cart for user: {}", username);
        shoppingCartService.deactivateCurrentShoppingCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeFromShoppingCart(@RequestParam @ValidUsername String username,
        @RequestBody @NotEmpty List<UUID> productIds) {
        log.info("Removing products from cart for user: {}, productIds: {}", username, productIds);
        return shoppingCartService.removeFromShoppingCart(username, productIds);
    }

    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam @ValidUsername String username,
        @RequestBody ChangeProductQuantityRequest request) {
        log.info("Changing product quantity for user: {}, request: {}", username, request);
        return shoppingCartService.changeProductQuantity(username, request);
    }
}
