package ru.yandex.practicum.product.service;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.*;

public interface ProductService {

    void newProductInWarehouse(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    void shipDelivery(ShippedToDeliveryRequest request);

    void returnProducts(@NotEmpty Map<UUID, Long> products);
}
