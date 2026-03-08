package ru.yandex.practicum.service.hub.handler;

import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.sensor.SensorService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler {

    private final SensorService sensorService;

    @Override
    public Class<?> getMessageType() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        if (event.getPayload() instanceof DeviceRemovedEventAvro deviceRemovedEvent) {
            sensorService.delete(deviceRemovedEvent.getId(), event.getHubId());
        }
    }
}
