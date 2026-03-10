package ru.yandex.practicum.repository;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.model.Scenario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    List<Scenario> findByHubId(String hubId);

    Optional<Scenario> findByHubIdAndName(String hubId, String name);
}
