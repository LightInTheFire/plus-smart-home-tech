package ru.yandex.practicum.service.condition;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Condition;

import java.util.List;
import java.util.Map;

public interface ConditionService {
    Map<String, Condition> processAvroConditions(List<ScenarioConditionAvro> conditionsAvro, String hubId);
}
