package ru.yandex.practicum.payment.controller;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.service.PaymentService;
import ru.yandex.practicum.shared.exceptions.NotEnoughInfoInOrderToCalculateException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@Valid @RequestBody OrderDto order) {
        log.info("Creating payment for order: {}", order.id());
        if (order.deliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("No delivery price in order");
        }
        return paymentService.createPayment(order);
    }

    @PostMapping("/totalCost")
    public BigDecimal getTotalCost(@Valid @RequestBody OrderDto order) {
        log.info("Calculating total cost for order: {}", order.id());
        if (order.products() == null || order.products()
            .isEmpty() || order.deliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough information in order to calculate cost");
        }
        return paymentService.getTotalCost(order);
    }

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void paymentSuccess(@NotNull @RequestBody UUID paymentId) {
        log.info("Processing successful payment for payment id: {}", paymentId);
        paymentService.updatePaymentSuccess(paymentId);
    }

    @PostMapping("/productCost")
    public BigDecimal productCost(@Valid @RequestBody OrderDto order) {
        log.info("Calculating product cost for order: {}", order.id());
        if (order.products() == null || order.products()
            .isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough information in order to calculate");
        }
        return paymentService.getProductCost(order);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void paymentFailed(@NotNull @RequestBody UUID paymentId) {
        log.info("Processing failed payment for payment id: {}", paymentId);
        paymentService.updatePaymentFailed(paymentId);
    }
}
