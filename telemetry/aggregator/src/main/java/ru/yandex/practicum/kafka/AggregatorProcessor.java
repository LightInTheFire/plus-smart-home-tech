package ru.yandex.practicum.kafka;

import java.time.Duration;
import java.util.List;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.service.AggregationService;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorProcessor implements CommandLineRunner {

    private final KafkaTopics kafkaTopics;
    private final KafkaProperties kafkaProperties;
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final AggregationService aggregationService;

    @Override
    public void run(String... args) {
        try {
            log.info("Subscribing consumer to topic {}", kafkaTopics.sensorEvents());
            consumer.subscribe(List.of(kafkaTopics.sensorEvents()));
            final Duration timeout = kafkaProperties.consumer()
                .consumeTimeout();

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(timeout);

                if (records.isEmpty()) {
                    log.info("No new records received after {}", timeout);
                }

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    aggregationService.handleEvent(record.value());
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
