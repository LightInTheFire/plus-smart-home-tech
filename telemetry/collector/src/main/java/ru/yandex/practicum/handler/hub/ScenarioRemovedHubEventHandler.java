package ru.yandex.practicum.handler.hub;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.EventTimestampKafkaProducer;
import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.mapper.HubEventMapper;

import org.springframework.stereotype.Component;

@Component
public class ScenarioRemovedHubEventHandler extends BaseHubEventHandler {

    public ScenarioRemovedHubEventHandler(HubEventMapper mapper, EventTimestampKafkaProducer producer,
        KafkaTopics kafkaTopics) {
        super(mapper, producer, kafkaTopics);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }
}
