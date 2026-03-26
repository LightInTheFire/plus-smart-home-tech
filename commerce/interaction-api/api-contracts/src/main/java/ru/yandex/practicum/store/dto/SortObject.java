package ru.yandex.practicum.store.dto;

public record SortObject(String direction, String nullHandling, Boolean ascending, String property,
    Boolean ignoreCase) {}
