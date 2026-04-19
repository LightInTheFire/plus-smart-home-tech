package ru.yandex.practicum.controller;

import java.util.List;

import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.service.OrderService;
import ru.yandex.practicum.validation.ValidUsername;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public OrderDto createNewOrder(@RequestBody CreateNewOrderRequest request,
        @RequestParam @ValidUsername String username) {
        log.info("Creating new order: {}, username : {}", request, username);
        return orderService.createNewOrder(request, username);
    }
}
