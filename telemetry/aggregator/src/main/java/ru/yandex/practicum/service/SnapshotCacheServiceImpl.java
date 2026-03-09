package ru.yandex.practicum.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotCacheServiceImpl implements SnapshotCacheService {

    private final Map<String, SensorsSnapshotAvro> hubsSensorsSnapshotsMap = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        if (event == null) {
            return Optional.empty();
        }

        SensorsSnapshotAvro sensorsSnapshotAvro = hubsSensorsSnapshotsMap.computeIfAbsent(
            event.getHubId(),
            hubId -> SensorsSnapshotAvro.newBuilder()
                .setHubId(hubId)
                .setTimestamp(event.getTimestamp())
                .setSensorsState(new HashMap<>())
                .build());

        SensorStateAvro oldState = sensorsSnapshotAvro.getSensorsState()
            .get(event.getId());
        if (oldState != null) {
            if (oldState.getTimestamp()
                .isAfter(event.getTimestamp())
                || oldState.getData()
                    .equals(event.getPayload())) {
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
            .setData(event.getPayload())
            .setTimestamp(event.getTimestamp())
            .build();
        sensorsSnapshotAvro.getSensorsState()
            .put(event.getId(), newState);
        sensorsSnapshotAvro.setTimestamp(event.getTimestamp());

        return Optional.of(sensorsSnapshotAvro);
    }
}
