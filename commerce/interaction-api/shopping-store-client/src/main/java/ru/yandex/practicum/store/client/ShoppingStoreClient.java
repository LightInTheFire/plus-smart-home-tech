package ru.yandex.practicum.store.client;

import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.store.dto.ProductDto;

import org.osimp.api.ReleaseConnection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient {

    @GetMapping("/{productId}")
    @ReleaseConnection
    ProductDto getProduct(@PathVariable UUID productId);

    @GetMapping("/products")
    @ReleaseConnection
    List<ProductDto> getProducts(@RequestParam List<UUID> productIds);
}
