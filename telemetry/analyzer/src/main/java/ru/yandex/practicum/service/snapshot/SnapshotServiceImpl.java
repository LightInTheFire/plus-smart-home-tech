package ru.yandex.practicum.service.snapshot;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.service.hub.HubRouterClient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.Timestamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

    private final HubRouterClient hubClient;
    private final ScenarioRepository scenarioRepository;

    @Override
    @Transactional
    public void analyzeSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);

        if (scenarios.isEmpty()) {
            log.info("No scenarios available for hub {}", hubId);
            return;
        }

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        Instant now = Instant.now();

        List<DeviceActionRequest> actionsForSend = scenarios.stream()
            .filter(sc -> isConditionsSatisfied(sc, sensorsState))
            .flatMap(
                sc -> sc.getActions()
                    .entrySet()
                    .stream()
                    .map(entry -> buildActionRequest(sc, entry, now)))
            .toList();

        if (actionsForSend.isEmpty()) {
            log.trace("No actions to send for hub {}", hubId);
            return;
        }

        hubClient.send(actionsForSend);
        log.debug("Sent {} actions for hub {}", actionsForSend.size(), hubId);
    }

    private boolean isConditionsSatisfied(Scenario scenario, Map<String, SensorStateAvro> sensorsState) {
        if (scenario.getConditions() == null || scenario.getConditions()
            .isEmpty()) {
            return false;
        }

        return scenario.getConditions()
            .entrySet()
            .stream()
            .allMatch(entry -> {
                SensorStateAvro state = sensorsState.get(entry.getKey());
                if (state == null) {
                    log.debug("Sensor {} not found in snapshot", entry.getKey());
                    return false;
                }
                return checkScenarioCondition(entry.getValue(), state);
            });
    }

    private DeviceActionRequest buildActionRequest(Scenario scenario, Map.Entry<String, Action> entry,
        Instant timestamp) {
        int value = entry.getValue()
            .getValue() != null ? entry.getValue()
                .getValue() : 0;

        DeviceActionProto actionProto = DeviceActionProto.newBuilder()
            .setSensorId(entry.getKey())
            .setType(
                ActionTypeProto.valueOf(
                    entry.getValue()
                        .getType()
                        .name()))
            .setValue(value)
            .build();

        return DeviceActionRequest.newBuilder()
            .setAction(actionProto)
            .setHubId(scenario.getHubId())
            .setScenarioName(scenario.getName())
            .setTimestamp(
                Timestamp.newBuilder()
                    .setSeconds(timestamp.getEpochSecond())
                    .setNanos(timestamp.getNano()))
            .build();
    }

    private boolean checkScenarioCondition(Condition condition, SensorStateAvro state) {
        int sensorValue = extractSensorValue(condition.getType(), state);
        return switch (condition.getOperation()) {
            case EQUALS -> sensorValue == condition.getValue();
            case GREATER_THAN -> sensorValue > condition.getValue();
            case LOWER_THAN -> sensorValue < condition.getValue();
        };
    }

    private int extractSensorValue(ConditionType type, SensorStateAvro state) {
        Object data = state.getData();
        if (data == null) {
            throw new IllegalArgumentException("Sensor data is null for type " + type);
        }

        return switch (type) {
            case MOTION -> ((MotionSensorAvro) data).getMotion() ? 1 : 0;
            case SWITCH -> ((SwitchSensorAvro) data).getState() ? 1 : 0;
            case LUMINOSITY -> ((LightSensorAvro) data).getLuminosity();
            case TEMPERATURE -> {
                if (data instanceof ClimateSensorAvro c) {
                    yield c.getTemperatureC();
                }
                if (data instanceof TemperatureSensorAvro t) {
                    yield t.getTemperatureC();
                }
                throw new IllegalArgumentException("Unknown temperature sensor type");
            }
            case CO2LEVEL -> ((ClimateSensorAvro) data).getCo2Level();
            case HUMIDITY -> ((ClimateSensorAvro) data).getHumidity();
        };
    }
}
