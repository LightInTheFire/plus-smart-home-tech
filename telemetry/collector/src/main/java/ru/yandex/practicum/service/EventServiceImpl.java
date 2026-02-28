package ru.yandex.practicum.service;

import ru.yandex.practicum.configuration.KafkaTopics;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.model.hub.*;
import ru.yandex.practicum.model.sensor.*;
import ru.yandex.practicum.serialization.AvroByteSerializer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final Producer<String, byte[]> producer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void publishHubEvent(HubEvent event) {
        HubEventAvro hubAvro = HubEventMapper.toAvro(event);
        log.info("Publishing hub event {}", hubAvro);
        byte[] bytes = AvroByteSerializer.serialize(hubAvro);

        producer.send(new ProducerRecord<>(kafkaTopics.hubEvents(), bytes));
    }

    @Override
    public void publishSensorEvent(SensorEvent event) {
        SensorEventAvro sensorAvro = SensorEventMapper.toAvro(event);
        log.info("Publishing sensor event {}", sensorAvro);
        byte[] bytes = AvroByteSerializer.serialize(sensorAvro);

        producer.send(new ProducerRecord<>(kafkaTopics.sensorEvents(), bytes));
    }
}
