package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.util.UUID;

import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.dto.DeliveryState;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.shared.exceptions.EntityNotFoundException;
import ru.yandex.practicum.warehouse.client.WarehouseClient;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final DeliveryCostCalculator deliveryCostCalculator;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto planDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDto);
        if (delivery.getState() == null) {
            delivery.setState(DeliveryState.CREATED);
        }

        Delivery saved = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(saved);
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.processDelivery(orderId);
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        orderClient.processAssembled(orderId);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        delivery.setState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.processDeliveryFailed(orderId);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto order) {
        Delivery delivery = getDeliveryByOrderId(order.id());
        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();
        return deliveryCostCalculator.calculateDeliveryCost(order, delivery, warehouseAddress);
    }

    private Delivery getDeliveryByOrderId(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Delivery not found with order id: " + orderId));
    }
}
