package ru.yandex.practicum.order.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import ru.yandex.practicum.order.dto.OrderState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "state", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "delivery_price", precision = 10, scale = 2)
    private BigDecimal deliveryPrice;

    @Column(name = "product_price", precision = 10, scale = 2)
    private BigDecimal productPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
