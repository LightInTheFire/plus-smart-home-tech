package ru.yandex.practicum.delivery.mapper;

import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.delivery.model.Delivery;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {

    @Mapping(source = "state", target = "deliveryState")
    @Mapping(source = "id", target = "deliveryId")
    DeliveryDto toDto(Delivery delivery);

    @Mapping(source = "deliveryState", target = "state")
    @Mapping(source = "deliveryId", target = "id")
    Delivery toEntity(DeliveryDto dto);
}
