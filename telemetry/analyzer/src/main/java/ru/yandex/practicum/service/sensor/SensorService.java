package ru.yandex.practicum.service.sensor;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public interface SensorService {

    void ensureSensorsExist(List<String> sensorIds, String hubId);

    void save(@NotBlank String sensorId, @NotBlank String hubId);

    void delete(@NotBlank String sensorId, @NotBlank String hubId);
}
