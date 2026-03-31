package ru.yandex.practicum.address;

import java.util.Optional;
import java.util.UUID;

import ru.yandex.practicum.address.model.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findFirstByOrderByIdAsc();
}
