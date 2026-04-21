package ru.yandex.practicum.product;

import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.product.model.OrderBooking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, UUID> {

    List<OrderBooking> findByOrderId(UUID orderId);
}
