package ru.yandex.practicum.model.sensor;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("TEMPERATURE_SENSOR_EVENT")
public record TemperatureSensorEvent(
        @NotBlank String id,
        @NotBlank String hubId,
        @NotNull Instant timestamp,
        int temperatureC,
        int temperatureF)
        implements SensorEvent {

    @Override
    public SensorEventType type() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
