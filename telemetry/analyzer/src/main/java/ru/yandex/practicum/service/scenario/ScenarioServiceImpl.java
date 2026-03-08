package ru.yandex.practicum.service.scenario;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.repository.ConditionRepository;
import ru.yandex.practicum.repository.ScenarioRepository;
import ru.yandex.practicum.service.action.ActionService;
import ru.yandex.practicum.service.condition.ConditionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ScenarioServiceImpl implements ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ActionService actionService;
    private final ConditionService conditionService;

    @Override
    public void save(@NotBlank String hubId, @NotNull ScenarioAddedEventAvro scenarioAddedEventAvro) {
        Scenario scenario = scenarioRepository.findByHubIdAndName(hubId, scenarioAddedEventAvro.getName())
            .orElseGet(
                () -> Scenario.builder()
                    .hubId(hubId)
                    .name(scenarioAddedEventAvro.getName())
                    .build());

        scenario.getActions()
            .clear();
        scenario.getConditions()
            .clear();

        List<DeviceActionAvro> actionsAvro = scenarioAddedEventAvro.getActions();
        List<ScenarioConditionAvro> conditionsAvro = scenarioAddedEventAvro.getConditions();

        Map<String, Action> actions = actionService.processAvroActions(actionsAvro, hubId);
        Map<String, Condition> conditions = conditionService.processAvroConditions(conditionsAvro, hubId);

        scenario.setActions(actions);
        scenario.setConditions(conditions);

        scenarioRepository.save(scenario);
        log.info("Saved new scenario {}", scenario.getName());
    }

    @Override
    public void delete(@NotBlank String hubId, @NotBlank String name) {

        Optional<Scenario> scenarioOptional = scenarioRepository.findByHubIdAndName(hubId, name);

        if (scenarioOptional.isEmpty()) {
            log.warn("Scenario with id {} and name {} not found", hubId, name);
            return;
        }

        Scenario scenario = scenarioOptional.get();
        log.info("Deleting scenario with id {} and name {}", hubId, name);
        scenarioRepository.deleteById(scenario.getId());

        List<Long> actionIdsToDelete = scenario.getActions()
            .values()
            .stream()
            .filter(Objects::nonNull)
            .map(Action::getId)
            .toList();
        List<Long> conditionIdsToDelete = scenario.getConditions()
            .values()
            .stream()
            .filter(Objects::nonNull)
            .map(Condition::getId)
            .toList();

        actionRepository.deleteAllById(actionIdsToDelete);
        conditionRepository.deleteAllById(conditionIdsToDelete);
    }
}
