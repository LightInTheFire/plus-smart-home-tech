package ru.yandex.practicum.address;

import ru.yandex.practicum.address.model.Address;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    AddressDto toAddressDto(Address address);

}
