package ru.yandex.practicum.service;

import ru.yandex.practicum.configuration.KafkaTopics;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.model.hub.*;
import ru.yandex.practicum.model.sensor.*;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void publishHubEvent(HubEvent event) {
        HubEventAvro hubAvro = HubEventMapper.toAvro(event);
        log.info("Publishing hub event {}", hubAvro);

        String key = switch (event) {
            case DeviceAddedEvent d -> d.hubId();
            case DeviceRemovedEvent d -> d.hubId();
            case ScenarioAddedEvent s -> s.hubId();
            case ScenarioRemovedEvent s -> s.hubId();
        };

        producer.send(new ProducerRecord<>(kafkaTopics.hubEvents(), key, hubAvro));
    }

    @Override
    public void publishSensorEvent(SensorEvent event) {
        SensorEventAvro sensorAvro = SensorEventMapper.toAvro(event);
        log.info("Publishing sensor event {}", sensorAvro);

        String key = switch (event) {
            case LightSensorEvent e -> e.hubId();
            case MotionSensorEvent e -> e.hubId();
            case ClimateSensorEvent e -> e.hubId();
            case TemperatureSensorEvent e -> e.hubId();
            case SwitchSensorEvent e -> e.hubId();
        };

        producer.send(new ProducerRecord<>(kafkaTopics.sensorEvents(), key, sensorAvro));
    }
}
