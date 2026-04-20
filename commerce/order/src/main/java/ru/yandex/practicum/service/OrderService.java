package ru.yandex.practicum.service;

import java.util.List;
import java.util.UUID;

import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.ProductReturnRequest;

public interface OrderService {

    List<OrderDto> getClientOrders(String username);

    OrderDto createNewOrder(CreateNewOrderRequest request, String username);

    OrderDto processReturn(ProductReturnRequest request);

    OrderDto processPayment(UUID orderId);

    OrderDto processPaymentFailed(UUID orderId);

    OrderDto processDelivery(UUID orderId);

    OrderDto processDeliveryFailed(UUID orderId);

    OrderDto processPaymentSucceed(UUID orderId);

    OrderDto complete(UUID orderId);

    OrderDto processAssembly(UUID orderId);

    OrderDto processAssemblyFailed(UUID orderId);

    OrderDto calculateTotalCost(UUID orderId);

    OrderDto calculateDeliveryCost(UUID orderId);
}
