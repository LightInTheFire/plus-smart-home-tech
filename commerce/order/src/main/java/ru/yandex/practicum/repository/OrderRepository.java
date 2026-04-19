package ru.yandex.practicum.repository;

import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUsernameLike(String username);

    List<Order> findByUsernameLikeOrderByCreatedAtDesc(String username);
}
