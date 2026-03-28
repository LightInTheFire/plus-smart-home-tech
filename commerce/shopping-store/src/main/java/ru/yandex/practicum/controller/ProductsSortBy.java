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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public enum ProductsSortBy {

    PRODUCT_ID("id", "id"),
    PRODUCT_NAME("productName", "name"),
    DESCRIPTION("description", "description"),
    IMAGE_SRC("imageSrc", "image_src"),
    QUANTITY_STATE("quantity", "quantity"),
    PRODUCT_STATE("state", "state"),
    PRODUCT_CATEGORY("category", "category"),
    PRICE("price", "price"),;

    private static final Map<String, ProductsSortBy> BY_NAME = Arrays.stream(values())
        .collect(Collectors.toUnmodifiableMap(ProductsSortBy::getName, Function.identity()));
    private static final Map<String, ProductsSortBy> BY_ENTITY_FIELD = Arrays.stream(values())
        .collect(Collectors.toUnmodifiableMap(ProductsSortBy::getEntityField, Function.identity()));
    @Getter
    private final String name;
    @Getter
    private final String entityField;

    public static String getNameByFieldName(String fieldName) {
        if (fieldName == null || fieldName.isBlank()) {
            return null;
        }

        ProductsSortBy sortBy = BY_ENTITY_FIELD.get(fieldName);
        return sortBy != null ? sortBy.getName() : null;
    }

    public static <T extends Throwable> List<Sort.Order> parseStrListOrThrow(List<String> list,
        Supplier<T> exceptionSupplier) throws T {

        if (list == null || list.isEmpty()) {
            return List.of();
        }

        List<Sort.Order> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {

            String value = list.get(i);
            if (value == null || value.isBlank()) {
                throw exceptionSupplier.get();
            }

            String field;
            Sort.Direction direction = Sort.Direction.ASC;

            if (value.contains(",")) {
                String[] parts = value.split(",", 2);

                field = parts[0].trim();

                if (parts.length == 2) {
                    direction = Sort.Direction.fromString(parts[1].trim());
                }

            } else {
                field = value.trim();

                if (i + 1 < list.size()) {
                    String next = list.get(i + 1);

                    if ("ASC".equalsIgnoreCase(next) || "DESC".equalsIgnoreCase(next)) {
                        direction = Sort.Direction.fromString(next);
                        i++;
                    }
                }
            }

            ProductsSortBy sortBy = BY_NAME.get(field);
            if (sortBy == null) {
                throw exceptionSupplier.get();
            }

            result.add(new Sort.Order(direction, sortBy.getEntityField()));
        }

        return result;
    }

}
