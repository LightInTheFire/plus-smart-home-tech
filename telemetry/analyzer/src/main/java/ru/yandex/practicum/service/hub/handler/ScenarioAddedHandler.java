package ru.yandex.practicum.service.hub.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.service.scenario.ScenarioService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScenarioAddedHandler implements HubEventHandler {

    private final ScenarioService scenarioService;

    @Override
    public Class<?> getMessageType() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        if (event.getPayload() instanceof ScenarioAddedEventAvro scenarioAddedEvent) {
            scenarioService.save(event.getHubId(), scenarioAddedEvent);
        }
    }
}
