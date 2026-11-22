package ru.yandex.practicum.delivery.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    UUID id;

    @Size(max = 64) @Column(name = "country", length = 64)
    String country;

    @Size(max = 120) @Column(name = "city", length = 120)
    String city;

    @Size(max = 120) @Column(name = "street", length = 120)
    String street;

    @Size(max = 120) @Column(name = "house", length = 120)
    String house;

    @Size(max = 120) @Column(name = "flat", length = 120)
    String flat;

}
