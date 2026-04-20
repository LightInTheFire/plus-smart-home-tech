package ru.yandex.practicum.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.*;

import ru.yandex.practicum.payment.dto.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "total_payment", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPayment;

    @Column(name = "delivery_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryTotal;

    @Column(name = "fee_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

}
