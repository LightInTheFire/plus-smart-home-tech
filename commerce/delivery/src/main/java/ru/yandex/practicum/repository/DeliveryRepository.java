package ru.yandex.practicum.repository;

import java.util.Optional;
import java.util.UUID;

import ru.yandex.practicum.model.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByOrderId(UUID orderId);
}
