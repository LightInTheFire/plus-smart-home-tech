package ru.yandex.practicum.delivery.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 64) @Column(name = "country", length = 64)
    private String country;

    @Size(max = 120) @Column(name = "city", length = 120)
    private String city;

    @Size(max = 120) @Column(name = "street", length = 120)
    private String street;

    @Size(max = 120) @Column(name = "house", length = 120)
    private String house;

    @Size(max = 120) @Column(name = "flat", length = 120)
    private String flat;

}
