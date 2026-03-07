package ru.yandex.practicum.kafka.config;

import java.util.Properties;

import jakarta.annotation.PreDestroy;

import ru.yandex.practicum.kafka.HubEventConsumerProperties;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.SnapshotConsumerProperties;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({KafkaProperties.class, HubEventConsumerProperties.class})
public class KafkaHubEventConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final HubEventConsumerProperties consumerProperties;
    private KafkaConsumer<String, HubEventAvro> consumer;

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubEventConsumer() {
        log.info("Initializing hub event kafka consumer");
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerProperties.clientId());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.groupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProperties.keyDeserializerClass());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerProperties.valueDeserializerClass());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerProperties.maxPollRecords());

        consumer = new KafkaConsumer<>(props);
        log.info("Kafka hub event consumer initialized");
        return consumer;
    }

    @PreDestroy
    public void wakeup() {
        consumer.wakeup();
    }
}
