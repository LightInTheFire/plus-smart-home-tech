package ru.yandex.practicum.handler.hub;

import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.mapper.HubEventMapper;
import ru.yandex.practicum.kafka.EventTimestampKafkaProducer;

import org.springframework.stereotype.Component;

@Component
public class DeviceRemovedHubEventHandler extends BaseHubEventHandler {

    public DeviceRemovedHubEventHandler(HubEventMapper mapper, EventTimestampKafkaProducer producer,
        KafkaTopics kafkaTopics) {
        super(mapper, producer, kafkaTopics);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

}
