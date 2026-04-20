package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ru.yandex.practicum.config.AppProperties;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import org.springframework.stereotype.Component;

@Component
public class DeliveryCostCalculator {

    private final BigDecimal baseRate;

    public DeliveryCostCalculator(AppProperties appProperties) {
        this.baseRate = appProperties.baseRate()
            .setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateDeliveryCost(OrderDto orderDto, Delivery delivery, AddressDto warehouseAddress) {
        BigDecimal total = baseRate;
        BigDecimal addressMultiplier = baseRate
            .multiply("ADDRESS_1".equals(warehouseAddress.city()) ? baseRate : baseRate.multiply(new BigDecimal(2)));
        total = total.add(addressMultiplier);

        BigDecimal fragileMultiplier = orderDto.fragile() ? total.multiply(new BigDecimal("0.2")) : BigDecimal.ZERO;
        total = total.add(fragileMultiplier);

        BigDecimal weightMultiplier = BigDecimal.valueOf(orderDto.deliveryWeight())
            .multiply(new BigDecimal("0.3"));
        total = total.add(weightMultiplier);

        BigDecimal volumeMultiplier = BigDecimal.valueOf(orderDto.deliveryVolume())
            .multiply(new BigDecimal("0.2"));
        total = total.add(volumeMultiplier);

        BigDecimal deliveryAddressMultiplier = warehouseAddress.street()
            .equals(
                delivery.getToAddress()
                    .getStreet()) ? BigDecimal.ZERO : total.multiply(new BigDecimal("0.2"));
        total = total.add(deliveryAddressMultiplier);

        return total;
    }
}
