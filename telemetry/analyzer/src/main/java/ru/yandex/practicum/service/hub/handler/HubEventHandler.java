package ru.yandex.practicum.service.hub.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {

    Class<?> getMessageType();

    void handle(HubEventAvro event);
}
