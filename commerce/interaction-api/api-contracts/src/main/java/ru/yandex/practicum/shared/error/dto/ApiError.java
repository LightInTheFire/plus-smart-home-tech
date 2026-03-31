package ru.yandex.practicum.shared.error.dto;

import org.springframework.http.HttpStatus;

public record ApiError(HttpStatus httpStatus, String userMessage) {}
