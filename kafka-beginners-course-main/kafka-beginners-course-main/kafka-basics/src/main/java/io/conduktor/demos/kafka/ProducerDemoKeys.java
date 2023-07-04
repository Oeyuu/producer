package io.conduktor.demos.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("i am a Kafka Producer!");
        // Create Producer Properties

        Properties properties = new Properties();
        // localhost
        properties.setProperty("bootstrap.servers", "localhost:9092");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int i = 0; i < 10; i++) {
            String topic = "demo_java";
            String key = "id_" + i;
            String value = "Hello " + i;

            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, value);

            // send data
            producer.send(producerRecord, new Callback() {

                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {

                    if (exception == null) {
                        log.info("Received new metadata \n" +
                                "Topic: " + key + "\n" + "Partition: " + metadata.partition() + "\n");
                    } else {
                        log.error("Error", exception);
                    }
                }
            });

        }

        // flush and close the producer
        producer.flush();
        producer.close();
    }
}