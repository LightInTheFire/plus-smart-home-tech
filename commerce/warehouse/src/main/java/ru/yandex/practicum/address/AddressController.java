package ru.yandex.practicum.address;

import ru.yandex.practicum.address.service.AddressService;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/address")
    public AddressDto getWarehouseAddress() {
        log.info("Getting warehouse address");
        return addressService.getWarehouseAddress();
    }
}
