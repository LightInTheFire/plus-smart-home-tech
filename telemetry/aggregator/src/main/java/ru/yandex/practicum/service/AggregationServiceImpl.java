package ru.yandex.practicum.service;

import java.util.Optional;

import ru.yandex.practicum.kafka.KafkaTopics;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregationServiceImpl implements AggregationService {

    private final SnapshotService snapshotService;
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final KafkaTopics kafkaTopics;

    @Override
    public void handleEvent(SensorEventAvro event) {
        Optional<SensorsSnapshotAvro> sensorsSnapshotAvro = snapshotService.updateState(event);

        sensorsSnapshotAvro.ifPresent((this::send));
    }

    private void send(SensorsSnapshotAvro sensorsSnapshotAvro) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(
            kafkaTopics.sensorSnapshots(),
            null,
            sensorsSnapshotAvro.getTimestamp()
                .toEpochMilli(),
            sensorsSnapshotAvro.getHubId(),
            sensorsSnapshotAvro);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send message to topic {}", kafkaTopics.sensorSnapshots(), exception);
            } else {
                log.info(
                    "Message successfully sent to topic {}, partition {}, offset {} with key {} ",
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset(),
                    sensorsSnapshotAvro.getHubId());
            }
        });

    }
}
