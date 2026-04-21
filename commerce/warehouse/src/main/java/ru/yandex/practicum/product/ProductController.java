package ru.yandex.practicum.product;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.product.service.ProductService;
import ru.yandex.practicum.warehouse.dto.*;

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
    public void newProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequest request) {
        log.info("Adding new product to warehouse: {}", request);
        productService.newProductInWarehouse(request);
    }

    @PostMapping("/check")
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(
        @Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("Checking product quantity for shopping cart: {}", shoppingCartDto);
        return productService.checkProductQuantityEnoughForShoppingCart(shoppingCartDto);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest request) {
        log.info("Adding product to warehouse: {}", request);
        productService.addProductToWarehouse(request);
    }

    @PostMapping("/assembly")
    public BookedProductsDto assemblyProducts(@Valid @RequestBody AssemblyProductsForOrderRequest request) {
        log.info("Assembling product for order: {}", request);
        return productService.assemblyProducts(request);
    }

    @PostMapping("/shipped")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void shipDelivery(@Valid @RequestBody ShippedToDeliveryRequest request) {
        log.info("Shipping delivery for order: {}", request);
        productService.shipDelivery(request);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnProducts(@NotEmpty Map<UUID, Long> products) {
        log.info("Returning products to warehouse: {}", products);
        productService.returnProducts(products);
    }
}
