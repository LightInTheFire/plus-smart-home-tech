package ru.yandex.practicum.shared.exceptions;

public class NoSpecifiedProductInWarehouseException extends RuntimeException {

    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }
}
