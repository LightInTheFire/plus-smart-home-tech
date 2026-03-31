package ru.yandex.practicum.service;

import java.util.List;

import ru.yandex.practicum.store.dto.ProductCategory;

import org.springframework.data.domain.Sort;

public record ProductsGetRequest(ProductCategory category, int page, int size, List<Sort.Order> sort) {}
