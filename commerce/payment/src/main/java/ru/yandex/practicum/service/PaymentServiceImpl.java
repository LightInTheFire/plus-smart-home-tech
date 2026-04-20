package ru.yandex.practicum.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.order.client.OrderClient;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;
import ru.yandex.practicum.payment.dto.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.shared.exceptions.EntityNotFoundException;
import ru.yandex.practicum.store.client.ShoppingStoreClient;
import ru.yandex.practicum.store.dto.ProductDto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;
    private final TaxCalculator taxCalculator;

    @Override
    public PaymentDto createPayment(OrderDto order) {
        BigDecimal productPrice = calculateProductCost(order);

        BigDecimal deliveryPrice = order.deliveryPrice();
        BigDecimal feeTotal = taxCalculator.calculateTax(productPrice);
        BigDecimal totalPayment = calculateTotalPayment(feeTotal, productPrice, deliveryPrice);

        Payment payment = new Payment();
        payment.setOrderId(order.id());
        payment.setTotalPayment(totalPayment);
        payment.setDeliveryTotal(deliveryPrice);
        payment.setFeeTotal(feeTotal);
        payment.setStatus(PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        log.info("Payment created with id: {}", saved.getId());

        return paymentMapper.toPaymentDto(saved);
    }

    @Override
    public BigDecimal getTotalCost(OrderDto order) {

        Payment payment = getPaymentById(order.paymentId());

        BigDecimal productPrice = calculateProductCost(order);
        BigDecimal feeTotal = taxCalculator.calculateTax(productPrice);
        BigDecimal deliveryPrice = order.deliveryPrice();
        BigDecimal totalPayment = calculateTotalPayment(feeTotal, productPrice, deliveryPrice);

        payment.setTotalPayment(totalPayment);
        payment.setDeliveryTotal(deliveryPrice);
        payment.setFeeTotal(feeTotal);

        return totalPayment;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getProductCost(OrderDto order) {
        return calculateProductCost(order);
    }

    @Override
    public void updatePaymentSuccess(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        orderClient.processPaymentSucceed(payment.getOrderId());

        log.info("Payment {} processed successfully", paymentId);
    }

    @Override
    public void updatePaymentFailed(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        orderClient.processPaymentFailed(payment.getOrderId());

        log.info("Payment {} failed", paymentId);
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new EntityNotFoundException("Payment not found: " + paymentId));
    }

    private BigDecimal calculateProductCost(OrderDto order) {
        if (order.products() == null || order.products()
            .isEmpty()) {
            return BigDecimal.ZERO;
        }

        return order.products()
            .entrySet()
            .stream()
            .map(entry -> {
                ProductDto product = shoppingStoreClient.getProduct(entry.getKey());
                return product.price()
                    .multiply(BigDecimal.valueOf(entry.getValue()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalPayment(BigDecimal tax, BigDecimal productPrice, BigDecimal deliveryPrice) {
        return productPrice.add(tax)
            .add(deliveryPrice)
            .setScale(2, RoundingMode.HALF_UP);
    }

}
