package ru.yandex.practicum.service.hub.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.service.scenario.ScenarioService;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedHandler implements HubEventHandler {

    private final ScenarioService scenarioService;

    @Override
    public Class<?> getMessageType() {
        return ScenarioRemovedEventAvro.class;
    }

    @Override
    public void handle(HubEventAvro event) {
        if (event.getPayload() instanceof ScenarioRemovedEventAvro scenarioRemovedEvent) {
            scenarioService.delete(event.getHubId(), scenarioRemovedEvent.getName());
        }
    }
}
