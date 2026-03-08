package ru.yandex.practicum.service.action;

import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.model.Action;

import java.util.List;
import java.util.Map;

public interface ActionService {
    Map<String, Action> processAvroActions(List<DeviceActionAvro> actionsAvro, String hubId);
}
