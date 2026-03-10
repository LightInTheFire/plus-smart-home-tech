package ru.yandex.practicum.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.EventTimestampKafkaProducer;
import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.mapper.SensorEventMapper;

import org.springframework.stereotype.Component;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler {

    public SwitchSensorEventHandler(SensorEventMapper mapper, EventTimestampKafkaProducer producer,
        KafkaTopics kafkaTopics) {
        super(mapper, producer, kafkaTopics);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

}
