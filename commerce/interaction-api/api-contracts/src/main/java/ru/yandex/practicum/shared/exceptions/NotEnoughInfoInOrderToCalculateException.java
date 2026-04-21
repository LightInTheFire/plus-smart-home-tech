package ru.yandex.practicum.shared.exceptions;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {

    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}
