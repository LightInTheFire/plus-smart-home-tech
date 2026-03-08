package ru.yandex.practicum.service.hub;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.hub.handler.HubEventHandler;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HubEventServiceImpl implements HubEventService {

    private final Map<Class<?>, HubEventHandler> hubEventHandlers;

    public HubEventServiceImpl(Set<HubEventHandler> hubEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
            .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void handle(HubEventAvro hubEvent) {
        if (hubEventHandlers.containsKey(hubEvent.getClass())) {
            hubEventHandlers.get(
                hubEvent.getPayload()
                    .getClass())
                .handle(hubEvent);
        } else {
            log.error("No handler for request with payload {}", hubEvent.getPayload());
        }
    }

}
