package ru.yandex.practicum.controller;

import ru.yandex.practicum.shared.error.dto.ApiError;
import ru.yandex.practicum.shared.exceptions.ProductNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ProductsExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiError handleApiException(Exception e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ApiError handleProductNotFoundException(Exception e) {
        log.warn("Product not found: {}", e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiError handleMissingServletRequestParameterException(Exception e) {
        log.warn("Missing request parameter: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
