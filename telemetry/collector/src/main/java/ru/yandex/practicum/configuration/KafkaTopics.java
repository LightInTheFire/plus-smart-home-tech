package ru.yandex.practicum.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kafka.topics")
public record KafkaTopics(String sensorEvents, String hubEvents) {
}
