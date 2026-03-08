package ru.yandex.practicum.kafka.processor;

import java.time.Duration;

import ru.yandex.practicum.kafka.HubEventConsumerProperties;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.hub.HubEventService;

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
public class HubEventProcessor implements ApplicationRunner {

    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final HubEventConsumerProperties properties;
    private final HubEventService hubEventService;

    @Override
    public void run(ApplicationArguments args) {
        new Thread(this::consume, "hub-event-consumer").start();
    }

    private void consume() {
        try {
            log.info("Subscribing consumer to topic {}", properties.topics());
            consumer.subscribe(properties.topics());
            final Duration timeout = properties.consumeTimeout();

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(timeout);

                if (records.isEmpty()) {
                    log.info("No new records received after {}", timeout);
                }

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    try {
                        hubEventService.handle(record.value());
                    } catch (Exception e) {
                        log.error("Exception during hub event processing {}", record, e);
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
