package ru.yandex.practicum.order.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.service.OrderService;
import ru.yandex.practicum.order.validation.ValidUsername;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getClientOrders(@RequestParam @ValidUsername String username) {
        log.info("Getting orders for user: {}", username);
        return orderService.getClientOrders(username);
    }

    @PutMapping
    public OrderDto createNewOrder(@Valid @RequestBody CreateNewOrderRequest request,
        @RequestParam @ValidUsername String username) {
        log.info("Creating new order: {}, username : {}", request, username);
        return orderService.createNewOrder(request, username);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@NotNull @RequestBody UUID orderId) {
        log.info("Calculating total cost for order: {}", orderId);
        return orderService.calculateTotalCost(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@NotNull @RequestBody UUID orderId) {
        log.info("Calculating delivery cost for order: {}", orderId);
        return orderService.calculateDeliveryCost(orderId);
    }
}
