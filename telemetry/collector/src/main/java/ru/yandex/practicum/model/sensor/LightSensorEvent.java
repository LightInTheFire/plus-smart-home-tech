package ru.yandex.practicum.model.sensor;

import java.time.Instant;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LIGHT_SENSOR_EVENT")
public record LightSensorEvent(
        @NotBlank String id,
        @NotBlank String hubId,
        @NotNull Instant timestamp,
        @Min(0) @Max(100) int linkQuality,
        @Min(0) int luminosity)
        implements
            SensorEvent {

    @Override
    public SensorEventType type() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
