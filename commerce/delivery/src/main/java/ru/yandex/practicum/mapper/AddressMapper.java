package ru.yandex.practicum.mapper;

import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    AddressDto toDto(Address address);

    Address toEntity(AddressDto dto);
}
