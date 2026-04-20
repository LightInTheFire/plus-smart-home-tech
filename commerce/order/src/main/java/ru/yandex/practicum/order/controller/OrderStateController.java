package ru.yandex.practicum.order.controller;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderStateController {

    private final OrderService orderService;

    @PostMapping("/return")
    public OrderDto productReturn(@RequestBody ProductReturnRequest request) {
        log.info("Processing return for order: {}", request.orderId());
        return orderService.processReturn(request);
    }

    @PostMapping("/payment")
    public OrderDto payment(@RequestBody @NotNull UUID orderId) {
        log.info("Processing payment for order: {}", orderId);
        return orderService.processPayment(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody @NotNull UUID orderId) {
        log.info("Payment failed for order: {}", orderId);
        return orderService.processPaymentFailed(orderId);
    }

    @PostMapping("/payment/succeed")
    public OrderDto paymentSucceed(@RequestBody @NotNull UUID orderId) {
        log.info("Payment succeed for order: {}", orderId);
        return orderService.processPaymentSucceed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto delivery(@RequestBody @NotNull UUID orderId) {
        log.info("Processing delivery for order: {}", orderId);
        return orderService.processDelivery(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody @NotNull UUID orderId) {
        log.info("Delivery failed for order: {}", orderId);
        return orderService.processDeliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto complete(@RequestBody @NotNull UUID orderId) {
        log.info("Completing order: {}", orderId);
        return orderService.complete(orderId);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestBody @NotNull UUID orderId) {
        log.info("Processing assembly for order: {}", orderId);
        return orderService.processAssembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody @NotNull UUID orderId) {
        log.info("Assembly failed for order: {}", orderId);
        return orderService.processAssemblyFailed(orderId);
    }
}
