package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ru.yandex.practicum.mapper.OrderAddressMapper;
import ru.yandex.practicum.mapper.OrderItemMapper;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderAddress;
import ru.yandex.practicum.model.OrderItem;
import ru.yandex.practicum.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderState;
import ru.yandex.practicum.order.dto.ProductReturnRequest;
import ru.yandex.practicum.payment.client.PaymentClient;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.repository.OrderAddressRepository;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.shared.exceptions.EntityNotFoundException;
import ru.yandex.practicum.shared.exceptions.NoSpecifiedProductInWarehouseException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderAddressMapper orderAddressMapper;
    private final OrderAddressRepository orderAddressRepository;
    private final PaymentClient paymentClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getClientOrders(String username) {
        log.info("Getting orders for user: {}", username);
        List<Order> orders = orderRepository.findByUsernameLikeOrderByCreatedAtDesc(username);
        return orders.stream()
            .map(orderMapper::toOrderDto)
            .toList();
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request, String username) {
        Map<UUID, Long> products = request.shoppingCart()
            .products();
        if (products == null || products.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setState(OrderState.NEW);
        order.setUsername(username);
        order.setCreatedAt(Instant.now());
        Set<OrderItem> orderItems = orderItemMapper.toOrderItems(order, products);
        order.setOrderItems(orderItems);
        OrderAddress orderAddress = orderAddressMapper.toOrderAddress(request.deliveryAddress());

        Order saved = orderRepository.save(order);

        orderAddress.setId(saved.getId());
        orderAddressRepository.save(orderAddress);
        log.info("Order created with id: {}", saved.getId());
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processReturn(ProductReturnRequest request) {
        Order order = getOrderById(request.orderId());

        Map<UUID, Long> returnProducts = request.products();
        for (Map.Entry<UUID, Long> entry : returnProducts.entrySet()) {
            order.getOrderItems()
                .stream()
                .filter(
                    i -> i.getProductId()
                        .equals(entry.getKey()))
                .findFirst()
                .ifPresent(item -> item.setQuantity(item.getQuantity() - entry.getValue()));
        }

        order.setState(OrderState.PRODUCT_RETURNED);
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processPayment(UUID orderId) {
        Order order = getOrderById(orderId);

        OrderDto orderDto = orderMapper.toOrderDto(order);
        PaymentDto payment = paymentClient.payment(orderDto);
        order.setPaymentId(payment.paymentId());
        order.setTotalPrice(payment.totalPayment());
        order.setState(OrderState.ON_PAYMENT);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processPaymentFailed(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setState(OrderState.PAYMENT_FAILED);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processPaymentSucceed(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setState(OrderState.PAID);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processDelivery(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setDeliveryId(UUID.randomUUID());
        order.setState(OrderState.ON_DELIVERY);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processDeliveryFailed(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setState(OrderState.DELIVERY_FAILED);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setState(OrderState.COMPLETED);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processAssembly(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setState(OrderState.ASSEMBLED);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto processAssemblyFailed(UUID orderId) {
        Order order = getOrderById(orderId);

        order.setState(OrderState.ASSEMBLY_FAILED);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = getOrderById(orderId);

        BigDecimal totalCost = calculateProductPrice(order)
            .add(order.getDeliveryPrice() != null ? order.getDeliveryPrice() : BigDecimal.ZERO); // todo goto delivery
                                                                                                 // service
        order.setTotalPrice(totalCost);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = getOrderById(orderId);

        BigDecimal deliveryCost = BigDecimal.ZERO; // todo goto delivery service
        order.setDeliveryPrice(deliveryCost);

        Order saved = orderRepository.save(order);
        return orderMapper.toOrderDto(saved);
    }

    private Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
    }

    private BigDecimal calculateProductPrice(Order order) {
        return order.getOrderItems()
            .stream()
            .map(item -> BigDecimal.valueOf(item.getQuantity() * 100))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
