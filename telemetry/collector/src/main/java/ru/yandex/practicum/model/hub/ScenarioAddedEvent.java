package ru.yandex.practicum.model.hub;

import java.time.Instant;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SCENARIO_ADDED")
public record ScenarioAddedEvent(
        @NotBlank String hubId,
        @NotNull Instant timestamp,
        @NotBlank String name,
        @Valid @NotEmpty List<ScenarioCondition> conditions,
        @Valid @NotEmpty List<DeviceAction> actions)
        implements HubEvent {

    @Override
    public HubEventType type() {
        return HubEventType.SCENARIO_ADDED;
    }
}
