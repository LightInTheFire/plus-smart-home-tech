package ru.yandex.practicum.shared.exceptions;

public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {

    public ProductInShoppingCartLowQuantityInWarehouse(String message) {
        super(message);
    }
}
