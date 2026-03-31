package ru.yandex.practicum.handler;

import ru.yandex.practicum.shared.error.dto.ApiError;
import ru.yandex.practicum.shared.exceptions.NoAddressesAvailableException;
import ru.yandex.practicum.shared.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.shared.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.shared.exceptions.SpecifiedProductAlreadyInWarehouseException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class WarehouseExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError handleException(Exception e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    public ApiError handleSpecifiedProductAlreadyInWarehouseException(SpecifiedProductAlreadyInWarehouseException e) {
        log.warn("Specified product already in warehouse: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
    public ApiError handleProductInShoppingCartLowQuantityInWarehouse(ProductInShoppingCartLowQuantityInWarehouse e) {
        log.warn("Product in shopping cart low quantity in warehouse: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ApiError handleNoSpecifiedProductInWarehouseException(NoSpecifiedProductInWarehouseException e) {
        log.warn("No specified product in warehouse: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoAddressesAvailableException.class)
    public ApiError handleNoAddressAvailableException(NoAddressesAvailableException e) {
        log.error("No addresses available: {}", e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
