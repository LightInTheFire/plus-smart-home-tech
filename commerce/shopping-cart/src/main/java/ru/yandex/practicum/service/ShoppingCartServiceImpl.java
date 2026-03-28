package ru.yandex.practicum.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ru.yandex.practicum.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.mapper.ShoppingCartMapper;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartItem;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.shared.exceptions.EntityNotFoundException;
import ru.yandex.practicum.shared.exceptions.NoProductsInShoppingCartException;
import ru.yandex.practicum.shared.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.warehouse.client.WarehouseClient;
import ru.yandex.practicum.warehouse.dto.BookedProductsDto;

import org.osimp.api.Osim;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        ShoppingCart userCart = shoppingCartRepository
            .findFirstWithItemsByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(username)
            .orElseThrow(
                () -> new EntityNotFoundException("No shopping cart found for username %s".formatted(username)));

        return shoppingCartMapper.toShoppingCartDto(userCart);
    }

    @Osim
    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        ShoppingCart userCart = shoppingCartRepository
            .findFirstWithItemsByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(username)
            .orElse(
                ShoppingCart.builder()
                    .username(username)
                    .createdAt(Instant.now())
                    .build());
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            ShoppingCartItem item = new ShoppingCartItem();
            item.setCart(userCart);
            item.setProductId(entry.getKey());
            item.setQuantity(entry.getValue());
            userCart.addItem(item);
        }

        ShoppingCart saved = shoppingCartRepository.save(userCart);
        ShoppingCartDto userCartDto = shoppingCartMapper.toShoppingCartDto(saved);
        try {
            BookedProductsDto bookedProductsDto = warehouseClient
                .checkProductQuantityEnoughForShoppingCart(userCartDto);
        } catch (FeignException.BadRequest e) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Not enough products in warehouse");
        }

        return userCartDto;
    }

    @Override
    public void deactivateCurrentShoppingCart(String username) {
        ShoppingCart userCart = shoppingCartRepository
            .findFirstByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(username)
            .orElseThrow(
                () -> new EntityNotFoundException("No shopping cart found for username %s".formatted(username)));

        userCart.setActive(false);
        log.info("Deactivated shopping cart with id {} for user {}", userCart.getId(), username);
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds) {
        ShoppingCart userCart = shoppingCartRepository
            .findFirstWithItemsByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(username)
            .orElseThrow(
                () -> new EntityNotFoundException("No shopping cart found for username %s".formatted(username)));

        for (UUID productId : productIds) {
            boolean isRemoved = userCart.removeItemByProductId(productId);
            if (!isRemoved) {
                throw new NoProductsInShoppingCartException(
                    "No product found with id %s in shopping cart".formatted(productId));
            }
        }

        return shoppingCartMapper.toShoppingCartDto(userCart);
    }

    @Osim
    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        ShoppingCart userCart = shoppingCartRepository
            .findFirstWithItemsByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(username)
            .orElseThrow(
                () -> new EntityNotFoundException("No shopping cart found for username %s".formatted(username)));

        boolean isItemInCart = userCart.getShoppingCartItems()
            .stream()
            .map(ShoppingCartItem::getProductId)
            .anyMatch(item -> item.equals(request.productId()));

        if (!isItemInCart) {
            throw new NoProductsInShoppingCartException(
                "No product with id: %s in shopping cart with id: %s".formatted(request.productId(), userCart.getId()));
        }

        userCart.updateItemQuantity(request.productId(), request.newQuantity());

        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toShoppingCartDto(userCart);

        try {
            BookedProductsDto bookedProductsDto = warehouseClient
                .checkProductQuantityEnoughForShoppingCart(shoppingCartDto);
        } catch (FeignException.BadRequest e) {
            throw new ProductInShoppingCartLowQuantityInWarehouse("Not enough products in warehouse");
        }

        log.info(
            "Quantity for item with id {} updated to {} in shopping cart with id {}",
            request.productId(),
            request.newQuantity(),
            userCart.getId());
        return shoppingCartDto;
    }
}
