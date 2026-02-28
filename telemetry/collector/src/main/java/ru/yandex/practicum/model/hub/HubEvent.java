package ru.yandex.practicum.model.hub;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
    @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
    @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
    @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
public sealed interface HubEvent
        permits DeviceAddedEvent, DeviceRemovedEvent, ScenarioAddedEvent, ScenarioRemovedEvent {

    @NotBlank String hubId();

    @NotNull Instant timestamp();

    @NotNull HubEventType type();
}
