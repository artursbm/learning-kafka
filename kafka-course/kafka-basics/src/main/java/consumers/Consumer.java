package consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {

    private static Logger log;

    private final String topic;

    private KafkaConsumer<String, String> kafkaConsumer;

    public Consumer(String groupId, String topic, Logger logger) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVER"));
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // to use partition assignment policy changed to an "iterative" method, where
        // new consumers will be assigned to a partition without needing to stop the world
        // (default method is to revoke every consumer's partitions, and then reassigning them)
        // properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        kafkaConsumer = new KafkaConsumer<>(properties);
        this.topic = topic;

        log = logger;
    }

    private void subscribeToTopic() {
        kafkaConsumer.subscribe(Collections.singletonList(this.topic));
    }

    public void poll() {
        try {
            if (kafkaConsumer.subscription().isEmpty()) subscribeToTopic();

            while (true) {
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
        } catch (WakeupException we) {
            log.info("Wakeup exception as expected!");
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            kafkaConsumer.close();
            log.info("the consumer has been successfully closed!");
        }
    }

    public void wakeup() {
        kafkaConsumer.wakeup();
    }


}
