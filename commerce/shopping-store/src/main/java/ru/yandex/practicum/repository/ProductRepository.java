package ru.yandex.practicum.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.store.dto.ProductCategory;
import ru.yandex.practicum.store.dto.ProductState;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByCategory(ProductCategory category, Pageable pageable);

    Page<Product> findByCategoryAndStateNot(ProductCategory category, ProductState state, Pageable pageable);

    List<Product> findByIdIn(Collection<UUID> ids);
}
