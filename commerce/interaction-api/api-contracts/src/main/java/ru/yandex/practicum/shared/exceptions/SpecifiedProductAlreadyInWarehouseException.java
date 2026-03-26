package ru.yandex.practicum.shared.exceptions;

public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {

    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }
}
