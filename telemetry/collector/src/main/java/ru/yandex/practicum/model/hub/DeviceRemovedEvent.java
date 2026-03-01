package ru.yandex.practicum.model.hub;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("DEVICE_REMOVED")
public record DeviceRemovedEvent(
        @NotBlank String hubId, @NotNull Instant timestamp, @NotBlank String id)
        implements
            HubEvent {

    @Override
    public HubEventType type() {
        return HubEventType.DEVICE_REMOVED;
    }
}
