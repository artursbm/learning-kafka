package producers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public abstract class Producer<T,S> {

    protected KafkaProducer<T,S> producer;

    public Producer() {
        // create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("BOOTSTRAP_SERVER"));
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // setting high throughput settings to improve producer's performance
        // linger.ms will set a linger time so producer can accumulate messages to batches
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        // batch size will be defined as 32KB (default is 16KB)
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024));
        // setting a compression algorithm increases throughput (using CPU cycles).
        // the bigger the batch, the better the compression performance
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        producer = new KafkaProducer<>(properties);
    }

    public void finishProducer() {
        producer.flush();
        producer.close();
    }
}
