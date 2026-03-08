package ru.yandex.practicum.service.condition;

import java.util.List;
import java.util.Map;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Condition;

public interface ConditionService {

    Map<String, Condition> processAvroConditions(List<ScenarioConditionAvro> conditionsAvro, String hubId);
}
