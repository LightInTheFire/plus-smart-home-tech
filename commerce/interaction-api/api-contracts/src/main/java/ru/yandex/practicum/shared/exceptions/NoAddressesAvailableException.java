package ru.yandex.practicum.shared.exceptions;

public class NoAddressesAvailableException extends RuntimeException {

    public NoAddressesAvailableException(String message) {
        super(message);
    }
}
