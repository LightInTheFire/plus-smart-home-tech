package ru.yandex.practicum.mapper;

import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.payment.dto.PaymentDto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);
}
