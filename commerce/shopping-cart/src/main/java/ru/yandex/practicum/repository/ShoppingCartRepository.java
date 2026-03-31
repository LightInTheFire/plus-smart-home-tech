package ru.yandex.practicum.repository;

import java.util.Optional;
import java.util.UUID;

import ru.yandex.practicum.model.ShoppingCart;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    Optional<ShoppingCart> findFirstByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(String username);

    @EntityGraph(attributePaths = "shoppingCartItems")
    Optional<ShoppingCart> findFirstWithItemsByUsernameLikeIgnoreCaseAndActiveTrueOrderByCreatedAtDesc(String username);
}
