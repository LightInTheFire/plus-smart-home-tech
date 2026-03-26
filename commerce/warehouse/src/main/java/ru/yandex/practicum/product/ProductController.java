package ru.yandex.practicum.product;

import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.product.service.ProductService;
import ru.yandex.practicum.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void newProductInWarehouse(@RequestBody NewProductInWarehouseRequest request) {
        log.info("Adding new product to warehouse: {}", request);
        productService.newProductInWarehouse(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("Checking product quantity for shopping cart: {}", shoppingCartDto);
        return productService.checkProductQuantityEnoughForShoppingCart(shoppingCartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@RequestBody AddProductToWarehouseRequest request) {
        log.info("Adding product to warehouse: {}", request);
        productService.addProductToWarehouse(request);
    }

}
