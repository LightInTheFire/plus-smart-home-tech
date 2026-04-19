package ru.yandex.practicum.mapper;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.OrderItem;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderState;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    default OrderDto toOrderDto(Order order) {
        if (order == null) {
            return null;
        }
        Map<UUID, Long> products = order.getOrderItems()
            .stream()
            .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

        OrderState state = order.getState();

        return new OrderDto(
            order.getId(),
            order.getShoppingCartId(),
            products,
            order.getPaymentId(),
            order.getDeliveryId(),
            state,
            order.getDeliveryWeight(),
            order.getDeliveryVolume(),
            order.getFragile(),
            order.getTotalPrice(),
            order.getDeliveryPrice(),
            order.getProductPrice());
    }
}
