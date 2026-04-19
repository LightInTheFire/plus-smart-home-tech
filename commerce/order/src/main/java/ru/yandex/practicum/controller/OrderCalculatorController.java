package ru.yandex.practicum.controller;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.service.OrderService;

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
@RequestMapping("/api/v1/order/calculate")
@RequiredArgsConstructor
public class OrderCalculatorController {

    private final OrderService orderService;

    @PostMapping("/total")
    public OrderDto calculateTotalCost(@RequestBody @NotNull UUID orderId) {
        log.info("Calculating total cost for order: {}", orderId);
        return orderService.calculateTotalCost(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody @NotNull UUID orderId) {
        log.info("Calculating delivery cost for order: {}", orderId);
        return orderService.calculateDeliveryCost(orderId);
    }
}
