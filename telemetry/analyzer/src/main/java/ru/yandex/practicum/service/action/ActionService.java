package ru.yandex.practicum.service.action;

import java.util.List;
import java.util.Map;

import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.model.Action;

public interface ActionService {

    Map<String, Action> processAvroActions(List<DeviceActionAvro> actionsAvro, String hubId);
}
