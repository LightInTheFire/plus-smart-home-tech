package ru.yandex.practicum.product.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

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

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}
