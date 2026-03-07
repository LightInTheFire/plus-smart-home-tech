package ru.yandex.practicum.kafka;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(String bootstrapServers, Consumer consumer, Producer producer) {

    public record Consumer(Class<?> keyDeserializerClass, Class<?> valueDeserializerClass, String clientId,
        String groupId, Integer maxPollRecords, Duration consumeTimeout) {}

    public record Producer(Class<?> keySerializerClass, Class<?> valueSerializerClass) {}
}
