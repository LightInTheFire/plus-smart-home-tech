package ru.yandex.practicum.address.model;

import java.util.UUID;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "id", nullable = false)
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
