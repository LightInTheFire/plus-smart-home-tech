package ru.yandex.practicum.mapper;

import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.model.OrderAddress;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderAddressMapper {

    OrderAddress toOrderAddress(@NotNull AddressDto addressDto);
}
