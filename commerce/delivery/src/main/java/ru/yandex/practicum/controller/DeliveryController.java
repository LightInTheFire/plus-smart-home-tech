package ru.yandex.practicum.controller;

import java.math.BigDecimal;
import java.util.UUID;

import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto planDelivery(@RequestBody DeliveryDto delivery) {
        log.info("Creating new delivery for order: {}", delivery.orderId());
        return deliveryService.planDelivery(delivery);
    }

    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deliverySuccessful(@RequestBody UUID orderId) {
        log.info("Marking delivery as successful for order: {}", orderId);
        deliveryService.deliverySuccessful(orderId);
    }

    @PostMapping("/picked")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deliveryPicked(@RequestBody UUID orderId) {
        log.info("Marking delivery as picked for order: {}", orderId);
        deliveryService.deliveryPicked(orderId);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deliveryFailed(@RequestBody UUID orderId) {
        log.info("Marking delivery as failed for order: {}", orderId);
        deliveryService.deliveryFailed(orderId);
    }

    @PostMapping("/cost")
    public BigDecimal deliveryCost(@RequestBody OrderDto order) {
        log.info("Calculating delivery cost for order: {}", order.id());
        return deliveryService.deliveryCost(order);
    }
}
