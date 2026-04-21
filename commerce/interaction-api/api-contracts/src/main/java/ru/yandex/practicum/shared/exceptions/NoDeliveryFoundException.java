package ru.yandex.practicum.shared.exceptions;

public class NoDeliveryFoundException extends RuntimeException {

    public NoDeliveryFoundException(String message) {
        super(message);
    }
}
