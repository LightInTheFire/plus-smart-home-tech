package ru.yandex.practicum.store.dto;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageProductDto(Long totalElements, Integer totalPages, Boolean first, Boolean last, Integer size,
    List<ProductDto> content, Integer number, Sort sort, Integer numberOfElements, Pageable pageable, Boolean empty) {}
