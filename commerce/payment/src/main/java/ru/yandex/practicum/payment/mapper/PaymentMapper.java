package ru.yandex.practicum.payment.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.dto.PaymentStatus;
import ru.yandex.practicum.payment.model.Payment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentDto toPaymentDto(Payment payment);

    default Payment toPayment(UUID orderId, BigDecimal totalPayment, BigDecimal deliveryTotal, BigDecimal feeTotal,
        PaymentStatus paymentStatus) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setTotalPayment(totalPayment);
        payment.setDeliveryTotal(deliveryTotal);
        payment.setFeeTotal(feeTotal);
        payment.setStatus(paymentStatus);

        return payment;
    }
}
