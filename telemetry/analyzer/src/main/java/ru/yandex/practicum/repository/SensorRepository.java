package ru.yandex.practicum.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.model.Sensor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

    List<Sensor> findAllByIdInAndHubId(Collection<String> ids, String hubId);
}
