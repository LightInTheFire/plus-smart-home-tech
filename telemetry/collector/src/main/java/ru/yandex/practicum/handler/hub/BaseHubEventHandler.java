package ru.yandex.practicum.handler.hub;

import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.util.Converter;
import ru.yandex.practicum.kafka.EventTimestampKafkaProducer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseHubEventHandler implements HubEventHandler {

    protected final HubEventMapper mapper;
    private final EventTimestampKafkaProducer producer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void handle(HubEventProto eventProto) {
        HubEventAvro eventAvro = mapper.map(eventProto);
        producer.send(
            kafkaTopics.hubEvents(),
            eventProto.getHubId(),
            Converter.timestampToMillis(eventProto.getTimestamp()),
            eventAvro);
    }
}
