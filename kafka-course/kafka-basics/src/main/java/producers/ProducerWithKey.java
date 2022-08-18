package producers;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;

import java.util.Objects;

public class ProducerWithKey extends Producer {

    private Logger log;

    public ProducerWithKey(Logger logger) {
        super();
        log = logger;
    }

    public void sendMessageWithKey(String topic, String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
        // send data with callback
        producer.send(producerRecord, (metadata, e) -> {
            if (Objects.isNull(e)) {
                log.info("The message has been sent to the topic. \n" +
                        "Topic: " + metadata.topic() + "\n" +
                        "Partition: " + metadata.partition() + "\n" +
                        "Offset: " + metadata.offset() + "\n" +
                        "Timestamp: " + metadata.timestamp() + "\n"
                );
            } else {
                log.error(e.getMessage());
            }
        });
    }


}
