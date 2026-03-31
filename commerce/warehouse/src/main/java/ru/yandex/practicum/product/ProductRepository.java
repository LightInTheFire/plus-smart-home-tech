package ru.yandex.practicum.product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import ru.yandex.practicum.product.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findByIdIn(Set<UUID> productIds);
}
