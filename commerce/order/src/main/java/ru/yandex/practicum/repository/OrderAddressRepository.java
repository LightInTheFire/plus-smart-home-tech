package ru.yandex.practicum.repository;

import java.util.UUID;

import ru.yandex.practicum.model.OrderAddress;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, UUID> {
}
