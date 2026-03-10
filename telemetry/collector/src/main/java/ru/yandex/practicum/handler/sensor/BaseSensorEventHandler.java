package ru.yandex.practicum.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.EventTimestampKafkaProducer;
import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.mapper.SensorEventMapper;
import ru.yandex.practicum.util.Converter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseSensorEventHandler implements SensorEventHandler {

    protected final SensorEventMapper mapper;
    private final EventTimestampKafkaProducer producer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void handle(SensorEventProto eventProto) {
        SensorEventAvro eventAvro = mapper.map(eventProto);
        producer.send(
            kafkaTopics.sensorEvents(),
            eventProto.getHubId(),
            Converter.timestampToMillis(eventProto.getTimestamp()),
            eventAvro);
        producer.flush();
    }
}
