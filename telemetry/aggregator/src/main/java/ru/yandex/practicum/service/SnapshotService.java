package ru.yandex.practicum.service;

import java.util.Optional;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotService {

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);
}
