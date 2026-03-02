package ru.yandex.practicum.configuration;

import java.util.Properties;

import jakarta.annotation.PreDestroy;

import ru.yandex.practicum.util.EventTimestampKafkaProducer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({KafkaTopics.class, KafkaProperties.class})
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;
    private EventTimestampKafkaProducer producer;

    @Bean
    public EventTimestampKafkaProducer kafkaProducer() {
        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.keySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.valueSerializerClass());

        producer = new EventTimestampKafkaProducer(props);
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
