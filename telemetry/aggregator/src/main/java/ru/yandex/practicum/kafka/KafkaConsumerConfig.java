package ru.yandex.practicum.kafka;

import java.util.Properties;

import jakarta.annotation.PreDestroy;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

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
@EnableConfigurationProperties({KafkaTopics.class, KafkaProperties.class})
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private KafkaConsumer<String, SensorEventAvro> consumer;

    @Bean
    public KafkaConsumer<String, SensorEventAvro> consumer() {
        log.info("Initializing kafka consumer");
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        props.put(
            ConsumerConfig.CLIENT_ID_CONFIG,
            kafkaProperties.consumer()
                .clientId());
        props.put(
            ConsumerConfig.GROUP_ID_CONFIG,
            kafkaProperties.consumer()
                .groupId());
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            kafkaProperties.consumer()
                .keyDeserializerClass());
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            kafkaProperties.consumer()
                .valueDeserializerClass());
        props.put(
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
            kafkaProperties.consumer()
                .maxPollRecords());

        consumer = new KafkaConsumer<>(props);
        log.info("Kafka consumer initialized");
        return consumer;
    }

    @PreDestroy
    public void wakeup() {
        consumer.wakeup();
    }
}
