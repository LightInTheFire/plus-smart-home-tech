package ru.yandex.practicum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import ru.yandex.practicum.shared.exceptions.NotAuthorizedUserException;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            throw new NotAuthorizedUserException("Username is null or empty");
        }
        return true;
    }
}
