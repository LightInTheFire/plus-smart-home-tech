package ru.yandex.practicum.service;

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
    public void send(DeviceActionRequest deviceActionRequest) {
        try {
            log.debug("Sending device action request with request {}", deviceActionRequest);
            hubRouterClient.handleDeviceAction(deviceActionRequest);
        } catch (StatusRuntimeException e) {
            log.error("Sending request to hub failed with request {}", deviceActionRequest, e);
        }
    }

}
