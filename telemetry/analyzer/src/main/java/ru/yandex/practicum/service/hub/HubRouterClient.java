package ru.yandex.practicum.service.hub;

import java.util.List;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;

public interface HubRouterClient {

    void send(List<DeviceActionRequest> deviceActionRequests);
}
