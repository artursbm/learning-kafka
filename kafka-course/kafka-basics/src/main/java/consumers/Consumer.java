package consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {

    private Logger log;

    private String topic;

    private KafkaConsumer<String, String> kafkaConsumer;

    public Consumer(String groupId, String topic, Logger logger) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVER"));
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        kafkaConsumer = new KafkaConsumer<>(properties);
        this.topic = topic;

        log = logger;
    }

    private void subscribeToTopic() {
        kafkaConsumer.subscribe(Collections.singletonList(this.topic));
    }

    public void poll() {
        if (kafkaConsumer.subscription().isEmpty()) subscribeToTopic();

        while (true) {
            log.info("Polling...");
            var records = kafkaConsumer.poll(Duration.ofMillis(1000));

            for (var record : records) {
                log.info("Record consumed from topic " + this.topic + ": \n" +
                        "Key: " + record.key() + "\n" +
                        "Value: " + record.value() + "\n" +
                        "Partition: " + record.partition() + "\n" +
                        "Offset: " + record.offset() + "\n" +
                        "Timestamp: " + record.timestamp() + "\n"
                );
            }
        }
    }


}
