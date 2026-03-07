package ru.yandex.practicum.kafka;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.consumer.hub-event")
public record HubEventConsumerProperties(List<String> topics, Class<?> keyDeserializerClass,
    Class<?> valueDeserializerClass, String clientId, String groupId, Integer maxPollRecords,
    Duration consumeTimeout) {}
