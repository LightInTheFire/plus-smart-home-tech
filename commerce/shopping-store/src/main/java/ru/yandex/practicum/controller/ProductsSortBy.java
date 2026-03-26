package ru.yandex.practicum.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductsSortBy {

    PRODUCT_ID("id", "id"),
    PRODUCT_NAME("name", "name"),
    DESCRIPTION("description", "description"),
    IMAGE_SRC("imageSrc", "image_src"),
    QUANTITY_STATE("quantity", "quantity"),
    PRODUCT_STATE("state", "state"),
    PRODUCT_CATEGORY("category", "category"),
    PRICE("price", "price"),;

    private static final Map<String, ProductsSortBy> BY_NAME = Arrays.stream(values())
        .collect(Collectors.toUnmodifiableMap(ProductsSortBy::getName, Function.identity()));
    @Getter
    private final String name;
    @Getter
    private final String entityField;

    public static <T extends Throwable> List<Sort.Order> parseStrListOrThrow(List<String> list,
        Supplier<T> exceptionSupplier) throws T {
        if (list == null || list.isEmpty()) {
            return List.of();
        }

        List<Sort.Order> result = new ArrayList<>(list.size());

        for (String value : list) {
            if (value == null || value.isBlank()) {
                throw exceptionSupplier.get();
            }

            String[] splitted = value.split(",", 2);

            ProductsSortBy sortBy = BY_NAME.get(splitted[0]);
            if (sortBy == null) {
                throw exceptionSupplier.get();
            }

            Sort.Direction direction = Sort.Direction.ASC;

            if (splitted.length == 2) {
                try {
                    direction = Sort.Direction.fromString(splitted[1]);
                } catch (IllegalArgumentException e) {
                    throw exceptionSupplier.get();
                }
            }

            result.add(new Sort.Order(direction, sortBy.getEntityField()));
        }

        return result;
    }
}
