package ru.yandex.practicum.service.condition;

import java.util.*;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.ConditionOperation;
import ru.yandex.practicum.model.ConditionType;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.service.sensor.SensorService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService {

    private final ConditionRepository conditionRepository;
    private final SensorService sensorService;

    @Override
    public Map<String, Condition> processAvroConditions(List<ScenarioConditionAvro> conditionsAvro, String hubId) {

        if (conditionsAvro == null || conditionsAvro.isEmpty()) {
            return Map.of();
        }

        List<String> sensorIds = conditionsAvro.stream()
            .map(ScenarioConditionAvro::getSensorId)
            .filter(id -> id != null && !id.isBlank())
            .toList();

        sensorService.ensureSensorsExist(sensorIds, hubId);

        List<Condition> conditionsToSave = conditionsAvro.stream()
            .filter(
                c -> c.getSensorId() != null && !c.getSensorId()
                    .isBlank())
            .map(conditionAvro -> {

                Object valueObj = conditionAvro.getValue();
                Integer value;

                if (valueObj instanceof Boolean bool) {
                    value = bool ? 1 : 0;
                } else if (valueObj instanceof Integer integer) {
                    value = integer;
                } else {
                    throw new IllegalArgumentException("Unsupported value type: " + valueObj.getClass());
                }

                return Condition.builder()
                    .operation(
                        ConditionOperation.valueOf(
                            conditionAvro.getOperation()
                                .name()))
                    .type(
                        ConditionType.valueOf(
                            conditionAvro.getType()
                                .name()))
                    .value(value)
                    .build();
            })
            .toList();

        List<Condition> savedConditions = conditionRepository.saveAll(conditionsToSave);

        Map<String, Condition> result = new HashMap<>();

        int index = 0;
        for (ScenarioConditionAvro conditionAvro : conditionsAvro) {

            String sensorId = conditionAvro.getSensorId();
            if (sensorId == null || sensorId.isBlank()) {
                continue;
            }

            result.put(sensorId, savedConditions.get(index++));
        }

        return result;
    }

}
