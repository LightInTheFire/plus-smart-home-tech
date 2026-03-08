package ru.yandex.practicum.service.hub;

import java.util.List;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

import org.springframework.stereotype.Component;

import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Slf4j
@Component
public class HubRouterClientImpl implements HubRouterClient {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public HubRouterClientImpl(
        @GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void send(List<DeviceActionRequest> deviceActionRequests) {
        try {
            for (DeviceActionRequest deviceActionRequest : deviceActionRequests) {
                log.trace("Sending device action request with request {}", deviceActionRequests);
                hubRouterClient.handleDeviceAction(deviceActionRequest);
            }
        } catch (StatusRuntimeException e) {
            log.error("Sending requests to hub failed", e);
        }
    }

}
