package ru.yandex.practicum.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;

public interface PaymentService {

    PaymentDto createPayment(OrderDto order);

    BigDecimal getTotalCost(OrderDto order);

    BigDecimal getProductCost(OrderDto order);

    void updatePaymentSuccess(UUID paymentId);

    void updatePaymentFailed(UUID paymentId);
}
