package ru.yandex.practicum.order.repository;

import java.util.UUID;

import ru.yandex.practicum.order.model.OrderAddress;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, UUID> {
}
