package ru.yandex.practicum.model.hub;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SCENARIO_REMOVED")
public record ScenarioRemovedEvent(@NotBlank String hubId, @NotNull Instant timestamp, @NotBlank String name)
    implements HubEvent {

    @Override
    public HubEventType type() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
