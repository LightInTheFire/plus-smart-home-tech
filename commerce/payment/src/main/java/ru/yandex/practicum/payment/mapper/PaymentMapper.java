package ru.yandex.practicum.payment.mapper;

import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.model.Payment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);
}
