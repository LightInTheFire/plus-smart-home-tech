package ru.yandex.practicum.model.sensor;

import java.time.Instant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("CLIMATE_SENSOR_EVENT")
public record ClimateSensorEvent(
        @NotBlank String id,
        @NotBlank String hubId,
        @NotNull Instant timestamp,
        int temperatureC,
        @Min(0) int humidity,
        @Min(0) int co2Level)
        implements SensorEvent {

    @Override
    public SensorEventType type() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
