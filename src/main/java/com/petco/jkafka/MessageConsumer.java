package com.petco.jkafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class MessageConsumer {

    public static void main(String[] args) {
        // Configure Kafka consumer properties
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:29092"); // Update with your Kafka broker address
        props.put("group.id", "test-group");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        // Create Kafka consumer
        Consumer<String, String> consumer = new KafkaConsumer<>(props);

        // Subscribe to Kafka topic(s)
        consumer.subscribe(Collections.singletonList("TEST_TOPIC")); // Update with your Kafka topic

        // Poll for new messages
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: key=%s, value=%s, partition=%d, offset=%d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
            }
        } finally {
            // Close the consumer when done
            consumer.close();
        }
    }
}
