import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producers.KeylessProducer;
import producers.ProducerWithKey;

public class KafkaDemoProgram {

    private static final Logger log = LoggerFactory.getLogger(KafkaDemoProgram.class.getSimpleName());

    public static void main(String[] args) {

        log.info("Hello, kafka World! I'm a producer.");


        KeylessProducer keylessProducer = new KeylessProducer(log);
        ProducerWithKey producerWithKey = new ProducerWithKey(log);
        for (int i = 0; i < 5; i++) {
            keylessProducer.sendKeylessMessage("messages_topic", "This is the " + i + " th keyless message");
            producerWithKey.sendMessageWithKey("messages_topic", "msg_id", "This is the " + i + " th message");
            // this is done in order to make kafka send messages to different partitions,
            // avoiding StickyPartition to send it to only one partition.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        producerWithKey.finishProducer();
        keylessProducer.finishProducer();

    }
}
