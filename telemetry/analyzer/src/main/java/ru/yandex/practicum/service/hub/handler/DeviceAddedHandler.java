package ru.yandex.practicum.service.hub.handler;

import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.sensor.SensorService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceAddedHandler implements HubEventHandler {

    private final SensorService sensorService;

    @Override
    public Class<?> getMessageType() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        if (event.getPayload() instanceof DeviceAddedEventAvro deviceAddedEvent) {
            sensorService.save(deviceAddedEvent.getId(), event.getHubId());
        }
    }
}
