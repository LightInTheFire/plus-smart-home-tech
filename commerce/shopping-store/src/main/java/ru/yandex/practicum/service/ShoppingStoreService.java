package ru.yandex.practicum.service;

import java.util.UUID;

import ru.yandex.practicum.store.dto.PageProductDto;
import ru.yandex.practicum.store.dto.ProductDto;
import ru.yandex.practicum.store.dto.SetProductQuantityStateRequest;

public interface ShoppingStoreService {

    PageProductDto getProducts(ProductsGetRequest getRequest);

    ProductDto createNewProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProductFromStore(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest request);

    ProductDto getProduct(UUID productId);
}
