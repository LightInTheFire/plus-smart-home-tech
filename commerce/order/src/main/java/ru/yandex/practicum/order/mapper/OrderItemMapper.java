package ru.yandex.practicum.order.mapper;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.model.OrderItem;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    default Set<OrderItem> toOrderItems(Order order, Map<UUID, Long> products) {
        if (order == null || products == null || products.isEmpty()) {
            return Set.of();
        }

        return products.entrySet()
            .stream()
            .map(entry -> {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(entry.getKey());
                item.setQuantity(entry.getValue());
                return item;
            })
            .collect(Collectors.toSet());
    }
}
