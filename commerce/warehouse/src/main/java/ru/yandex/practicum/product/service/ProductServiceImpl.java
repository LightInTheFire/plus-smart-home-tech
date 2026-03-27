package ru.yandex.practicum.product.service;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.*;

import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.product.ProductMapper;
import ru.yandex.practicum.product.ProductRepository;
import ru.yandex.practicum.product.model.Product;
import ru.yandex.practicum.shared.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.shared.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.shared.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.warehouse.dto.BookedProductsDto;
import ru.yandex.practicum.warehouse.dto.NewProductInWarehouseRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        if (productRepository.existsById(request.productId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                "Product with id: %s already exists".formatted(request.productId()));
        }

        Product product = productMapper.toProduct(request);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> products = shoppingCartDto.products();
        List<Product> productsStock = productRepository.findByIdIn(products.keySet());
        if (products.size() != productsStock.size()) {
            Set<UUID> existingProductKeys = productsStock.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
            List<UUID> notExistingProductKeys = products.keySet()
                .stream()
                .filter(e -> !existingProductKeys.contains(e))
                .toList();
            throw new NoSpecifiedProductInWarehouseException(
                "Products with id's %s from shopping cart are not exist".formatted(notExistingProductKeys.toString()));
        }

        boolean isEnoughStock = productsStock.stream()
            .allMatch(p -> p.getQuantity() >= products.get(p.getId()));
        if (!isEnoughStock) {
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                "Not enough stock for some products from shopping cart");
        }

        boolean isFragile = productsStock.stream()
            .anyMatch(Product::isFragile);

        double deliveryWeight = productsStock.stream()
            .map(
                product -> product.getWeight()
                    .multiply(BigDecimal.valueOf(products.get(product.getId()))))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .doubleValue();

        double deliveryVolume = productsStock.stream()
            .map(
                product -> product.getDimension()
                    .calculateVolume()
                    .multiply(BigDecimal.valueOf(products.get(product.getId()))))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .doubleValue();

        return new BookedProductsDto(deliveryWeight, deliveryVolume, isFragile);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        productRepository.findById(request.productId())
            .ifPresentOrElse((product) -> {
                Long currentQuantity = product.getQuantity();
                long updatedQuantity = currentQuantity + request.quantity();
                product.setQuantity(updatedQuantity);
                log.info("Adding {} to product {}", request.quantity(), request.productId());
            }, () -> {
                throw new NoSpecifiedProductInWarehouseException(
                    "Product with id: %s does not exist".formatted(request.productId()));
            });
    }

}
