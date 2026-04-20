package ru.yandex.practicum.delivery.service;

import java.math.BigDecimal;
import java.util.UUID;

import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;

public interface DeliveryService {

    DeliveryDto planDelivery(DeliveryDto delivery);

    void deliverySuccessful(UUID orderId);

    void deliveryPicked(UUID orderId);

    void deliveryFailed(UUID orderId);

    BigDecimal deliveryCost(OrderDto order);
}
