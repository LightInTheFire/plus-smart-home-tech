package ru.yandex.practicum.kafka.processor;

import java.time.Duration;

import ru.yandex.practicum.kafka.HubEventConsumerProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.snapshot.SnapshotService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements ApplicationRunner {

    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final HubEventConsumerProperties properties;
    private final SnapshotService snapshotService;

    @Override
    public void run(ApplicationArguments args) {
        new Thread(this::consume, "snapshot-consumer").start();
    }

    private void consume() {
        try {
            log.info("Subscribing consumer to topic {}", properties.topics());
            consumer.subscribe(properties.topics());
            final Duration timeout = properties.consumeTimeout();

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(timeout);

                if (records.isEmpty()) {
                    log.info("No new records received after {}", timeout);
                }

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    try {
                        snapshotService.analyzeSnapshot(record.value());
                    } catch (Exception e) {
                        log.error("Exception during snapshot processing {}", record, e);
                    }
                }

                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {} finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Closing kafka consumer");
                consumer.close();
            }
        }
    }

}
