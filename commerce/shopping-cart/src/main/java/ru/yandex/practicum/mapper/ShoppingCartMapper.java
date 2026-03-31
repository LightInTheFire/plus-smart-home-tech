package ru.yandex.practicum.mapper;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.model.ShoppingCartItem;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {

    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

    default ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart) {
        Map<UUID, Long> products = shoppingCart.getShoppingCartItems()
            .stream()
            .collect(Collectors.toMap(ShoppingCartItem::getProductId, ShoppingCartItem::getQuantity));
        return new ShoppingCartDto(shoppingCart.getId(), products);
    }
}
