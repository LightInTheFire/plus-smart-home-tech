package ru.yandex.practicum.repository;

import java.util.UUID;

import ru.yandex.practicum.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Payment findByOrderId(UUID orderId);
}
