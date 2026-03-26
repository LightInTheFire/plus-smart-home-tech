package ru.yandex.practicum.controller;

import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.service.ProductsGetRequest;
import ru.yandex.practicum.service.ShoppingStoreService;
import ru.yandex.practicum.store.dto.PageProductDto;
import ru.yandex.practicum.store.dto.ProductCategory;
import ru.yandex.practicum.store.dto.ProductDto;
import ru.yandex.practicum.store.dto.SetProductQuantityStateRequest;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {

    private final ShoppingStoreService shoppingStoreService;

    @GetMapping
    public PageProductDto getProducts(@RequestParam ProductCategory category,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
        @RequestParam(required = false) List<String> sort) {

        log.info("Getting products for category: {}, page: {}, size: {}, sort: {}", category, page, size, sort);

        List<Sort.Order> sortBy = ProductsSortBy.parseStrListOrThrow(
            sort,
            () -> new IllegalArgumentException("Invalid sort parameter %s".formatted(sort.toString())));

        ProductsGetRequest getRequest = new ProductsGetRequest(category, page, size, sortBy);
        return shoppingStoreService.getProducts(getRequest);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createNewProduct(@RequestBody ProductDto productDto) {

        log.info("Creating new product: {}", productDto);

        return shoppingStoreService.createNewProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody ProductDto productDto) {

        log.info("Updating product: {}", productDto.id());

        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean removeProductFromStore(@RequestBody UUID productId) {

        log.info("Removing product from store: {}", productId);

        return shoppingStoreService.removeProductFromStore(productId);
    }

    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(@RequestBody SetProductQuantityStateRequest request) {

        log.info("Setting quantity state for product: {} to state: {}", request.productId(), request.quantityState());

        return shoppingStoreService.setProductQuantityState(request);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {

        log.info("Getting product details: {}", productId);

        return shoppingStoreService.getProduct(productId);
    }
}
