package ru.yandex.practicum.service;

import java.util.Optional;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotCacheService {

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);
}
