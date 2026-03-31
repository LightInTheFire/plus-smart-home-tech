package ru.yandex.practicum.store.dto;

import java.util.List;

import org.springframework.data.domain.Pageable;

public record PageProductDto(Long totalElements, Integer totalPages, Boolean first, Boolean last, Integer size,
    List<ProductDto> content, Integer number, List<SortObject> sort, Integer numberOfElements, Pageable pageable,
    Boolean empty) {}
