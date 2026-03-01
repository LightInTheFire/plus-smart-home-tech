package ru.yandex.practicum.service;

import ru.yandex.practicum.configuration.KafkaTopics;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.model.hub.*;
import ru.yandex.practicum.model.sensor.*;
import ru.yandex.practicum.util.EventTimestampKafkaProducer;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventTimestampKafkaProducer producer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void publishHubEvent(HubEvent event) {
        HubEventAvro hubAvro = HubEventMapper.toAvro(event);

        String key = switch (event) {
            case DeviceAddedEvent d -> d.hubId();
            case DeviceRemovedEvent d -> d.hubId();
            case ScenarioAddedEvent s -> s.hubId();
            case ScenarioRemovedEvent s -> s.hubId();
        };

        long timestamp = event.timestamp()
            .toEpochMilli();

        producer.send(kafkaTopics.hubEvents(), key, timestamp, hubAvro);
    }

    @Override
    public void publishSensorEvent(SensorEvent event) {
        SensorEventAvro sensorAvro = SensorEventMapper.toAvro(event);

        String key = switch (event) {
            case LightSensorEvent e -> e.hubId();
            case MotionSensorEvent e -> e.hubId();
            case ClimateSensorEvent e -> e.hubId();
            case TemperatureSensorEvent e -> e.hubId();
            case SwitchSensorEvent e -> e.hubId();
        };

        long timestamp = event.timestamp()
            .toEpochMilli();

        producer.send(kafkaTopics.sensorEvents(), key, timestamp, sensorAvro);
    }
}
