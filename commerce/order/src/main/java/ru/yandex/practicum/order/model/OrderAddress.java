package ru.yandex.practicum.order.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_address")
public class OrderAddress {

    @Id
    @Column(name = "order_id", nullable = false)
    private UUID id;

    @Column(name = "country", length = 64)
    private String country;

    @Column(name = "city", length = 120)
    private String city;

    @Column(name = "street", length = 120)
    private String street;

    @Column(name = "house", length = 120)
    private String house;

    @Column(name = "flat", length = 120)
    private String flat;

}
