package ru.yandex.practicum.payment.repository;

import java.util.UUID;

import ru.yandex.practicum.payment.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Payment findByOrderId(UUID orderId);
}
