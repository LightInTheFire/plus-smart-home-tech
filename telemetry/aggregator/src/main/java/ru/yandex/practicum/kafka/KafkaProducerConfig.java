package ru.yandex.practicum.kafka;

import java.util.Properties;

import jakarta.annotation.PreDestroy;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({KafkaTopics.class, KafkaProperties.class})
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, SpecificRecordBase> producer;

    @Bean
    public KafkaProducer<String, SpecificRecordBase> kafkaProducer() {
        log.info("Initializing kafka producer");
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        props.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            kafkaProperties.producer()
                .keySerializerClass());
        props.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            kafkaProperties.producer()
                .valueSerializerClass());

        producer = new KafkaProducer<>(props);
        log.info("Kafka producer initialized");
        return producer;
    }

    @PreDestroy
    public void closeProducer() {
        log.info("Closing kafka producer");
        if (producer != null) {
            producer.flush();
            producer.close();
        }
    }
}
