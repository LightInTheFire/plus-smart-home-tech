package ru.yandex.practicum.service.scenario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

public interface ScenarioService {

    void save(@NotBlank String hubId, @NotNull ScenarioAddedEventAvro scenarioAddedEventAvro);

    void delete(@NotBlank String hubId, @NotBlank String name);
}
