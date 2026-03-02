package ru.yandex.practicum.model.sensor;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SWITCH_SENSOR_EVENT")
public record SwitchSensorEvent(@NotBlank String id, @NotBlank String hubId, @NotNull Instant timestamp, boolean state)
    implements SensorEvent {

    @Override
    public SensorEventType type() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
