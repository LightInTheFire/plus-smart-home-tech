package ru.yandex.practicum.address.service;

import ru.yandex.practicum.address.AddressMapper;
import ru.yandex.practicum.address.AddressRepository;
import ru.yandex.practicum.address.model.Address;
import ru.yandex.practicum.shared.exceptions.NoAddressesAvailableException;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public AddressDto getWarehouseAddress() {
        Address address = addressRepository.findFirstByOrderByIdAsc()
            .orElseThrow(() -> new NoAddressesAvailableException("No warehouse addresses currently available"));
        return addressMapper.toAddressDto(address);
    }
}
