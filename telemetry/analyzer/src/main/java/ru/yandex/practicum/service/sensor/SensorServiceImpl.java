package ru.yandex.practicum.service.sensor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;

import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repository.SensorRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    @Override
    public void ensureSensorsExist(List<String> sensorIds, String hubId) {
        if (sensorIds == null || sensorIds.isEmpty()) {
            return;
        }

        List<String> uniqueIds = sensorIds.stream()
            .filter(id -> id != null && !id.isBlank())
            .distinct()
            .toList();

        List<Sensor> existingSensors = sensorRepository.findAllByIdInAndHubId(uniqueIds, hubId);

        Set<String> existingIds = existingSensors.stream()
            .map(Sensor::getId)
            .collect(Collectors.toSet());

        List<Sensor> sensorsToSave = uniqueIds.stream()
            .filter(id -> !existingIds.contains(id))
            .map(
                id -> Sensor.builder()
                    .id(id)
                    .hubId(hubId)
                    .build())
            .toList();

        if (!sensorsToSave.isEmpty()) {
            sensorRepository.saveAll(sensorsToSave);
        }
    }

    @Override
    public void save(@NotBlank String sensorId, @NotBlank String hubId) {
        Sensor sensor = Sensor.builder()
            .id(sensorId)
            .hubId(hubId)
            .build();
        try {
            sensorRepository.save(sensor);
            log.info("Saved sensor with id {} and hub id {}", sensorId, hubId);
        } catch (DataIntegrityViolationException e) {
            log.warn("Sensor with id {} and already exists", sensorId);
        }
    }

    @Override
    public void delete(@NotBlank String sensorId, @NotBlank String hubId) {
        sensorRepository.findByIdAndHubId(sensorId, hubId)
            .ifPresentOrElse(
                sensor -> log.info("Deleting sensor with id {}", sensorId),
                () -> log.warn("Sensor with id {} not found", sensorId));
        sensorRepository.deleteById(sensorId);
    }
}
