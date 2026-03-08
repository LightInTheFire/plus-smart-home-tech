package ru.yandex.practicum.service.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.ActionType;
import ru.yandex.practicum.repository.ActionRepository;
import ru.yandex.practicum.service.sensor.SensorService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final SensorService sensorService;

    @Override
    public Map<String, Action> processAvroActions(List<DeviceActionAvro> actionsAvro, String hubId) {

        List<String> sensorIds = actionsAvro.stream()
            .map(DeviceActionAvro::getSensorId)
            .filter(id -> id != null && !id.isBlank())
            .toList();

        sensorService.ensureSensorsExist(sensorIds, hubId);

        List<Action> actionsToSave = actionsAvro.stream()
            .filter(
                a -> a.getSensorId() != null && !a.getSensorId()
                    .isBlank())
            .map(
                a -> Action.builder()
                    .type(
                        ActionType.valueOf(
                            a.getType()
                                .name()))
                    .value(a.getValue())
                    .build())
            .toList();

        List<Action> saved = actionRepository.saveAll(actionsToSave);

        Map<String, Action> result = new HashMap<>();

        int i = 0;
        for (DeviceActionAvro action : actionsAvro) {
            if (action.getSensorId() == null || action.getSensorId()
                .isBlank()) {
                continue;
            }
            result.put(action.getSensorId(), saved.get(i++));
        }

        return result;
    }

}
