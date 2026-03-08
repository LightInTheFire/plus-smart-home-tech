package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;

public interface HubRouterClient {

    void send(DeviceActionRequest deviceActionRequest);
}
