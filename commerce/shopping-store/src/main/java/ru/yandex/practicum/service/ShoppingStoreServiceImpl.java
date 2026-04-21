package ru.yandex.practicum.service;

import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.controller.ProductsSortBy;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.shared.exceptions.ProductNotFoundException;
import ru.yandex.practicum.store.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public PageProductDto getProducts(ProductsGetRequest getRequest) {

        Sort sort = Sort.by(getRequest.sort());
        Pageable pageable = PageRequest.of(getRequest.page(), getRequest.size(), sort);
        Page<Product> products = productRepository
            .findByCategoryAndStateNot(getRequest.category(), ProductState.DEACTIVATE, pageable);

        List<ProductDto> productDtos = products.stream()
            .map(productMapper::toProductDto)
            .toList();
        List<SortObject> sortOrders = products.getSort()
            .stream()
            .map(
                order -> new SortObject(
                    ProductsSortBy.getNameByFieldName(order.getProperty()),
                    order.getDirection()
                        .name()))
            .toList();
        return new PageProductDto(
            products.getTotalElements(),
            products.getTotalPages(),
            products.isFirst(),
            products.isLast(),
            products.getSize(),
            productDtos,
            products.getNumber(),
            sortOrders,
            products.getNumberOfElements(),
            products.getPageable(),
            products.isEmpty());
    }

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        return productMapper.toProductDto(saved);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product existingProduct = getProductOrThrow(productDto.id());
        productMapper.updateProductFromDto(productDto, existingProduct);
        Product saved = productRepository.save(existingProduct);
        log.info("Product with id: {} has been updated", existingProduct.getId());
        return productMapper.toProductDto(saved);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        Product product = getProductOrThrow(productId);
        product.setState(ProductState.DEACTIVATE);
        log.info("Product with id {} has been deactivated", productId);
        return true;
    }

    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProductOrThrow(request.productId());
        product.setQuantity(request.quantityState());
        log.info("Quantity state of product with id {} changed to: {}", request.productId(), request.quantityState());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(UUID productId) {
        Product product = getProductOrThrow(productId);
        return productMapper.toProductDto(product);
    }

    @Override
    public List<ProductDto> getProductsByIds(List<UUID> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        return products.stream()
            .map(productMapper::toProductDto)
            .toList();
    }

    private Product getProductOrThrow(UUID productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product with id: %s not found".formatted(productId)));
    }
}
