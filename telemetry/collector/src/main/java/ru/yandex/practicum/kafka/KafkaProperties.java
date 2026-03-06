package ru.yandex.practicum.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka")
public record KafkaProperties(String bootstrapServers, Class<?> keySerializerClass, Class<?> valueSerializerClass) {}
