package ru.yandex.practicum.util;

import java.util.Properties;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventTimestampKafkaProducer extends KafkaProducer<String, SpecificRecordBase> {

    public EventTimestampKafkaProducer(Properties properties) {
        super(properties);
    }

    public void send(String topic, String key, long timestamp, SpecificRecordBase value) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, null, timestamp, key, value);
        super.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.error("Failed to send message to topic {}", topic, exception);
            } else {
                log.info(
                    "Message successfully sent to topic {}, partition {}, offset {} with key {} ",
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset(),
                    key);
            }
        });
    }
}
