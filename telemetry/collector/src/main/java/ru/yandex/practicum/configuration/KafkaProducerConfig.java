package ru.yandex.practicum.configuration;

import java.util.Properties;

import jakarta.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({KafkaTopics.class, KafkaProperties.class})
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, byte[]> producer;

    @Bean
    public Producer<String, byte[]> kafkaProducer() {
        Properties props = new Properties();

        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaProperties.bootstrapServers()
        );
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                ByteArraySerializer.class
        );

        producer = new KafkaProducer<>(props);
        return producer;
    }

    @PreDestroy
    public void closeProducer() {
        if (producer != null) {
            producer.flush();
            producer.close();
        }
    }
}
