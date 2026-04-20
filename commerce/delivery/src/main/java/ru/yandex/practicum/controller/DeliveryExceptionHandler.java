package ru.yandex.practicum.controller;

import ru.yandex.practicum.shared.error.dto.ApiError;
import ru.yandex.practicum.shared.exceptions.EntityNotFoundException;
import ru.yandex.practicum.shared.exceptions.NoDeliveryFoundException;
import ru.yandex.practicum.shared.exceptions.NotAuthorizedUserException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class DeliveryExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError handleException(Exception e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotAuthorizedUserException.class)
    public ApiError handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        log.warn("Not authorized user: {}", e.getMessage());
        return new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("Missing request parameter: {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiError handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoDeliveryFoundException.class)
    public ApiError handleNoDeliveryFoundException(NoDeliveryFoundException e) {
        log.warn("Delivery not found: {}", e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
