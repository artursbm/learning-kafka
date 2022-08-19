import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producers.KeylessProducer;
import producers.ProducerWithKey;

public class ProducerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {

        log.info("Hello, kafka World! I'm a producer.");


        var keylessProducer = new KeylessProducer(log);
        var producerWithKey = new ProducerWithKey(log);
        for (int i = 0; i < 5; i++) {
            // keylessProducer.sendKeylessMessage(System.getenv("TOPIC"), "This is the " + i + " th keyless message");
            producerWithKey.sendMessageWithKey(System.getenv("TOPIC"), "msg_id", "This is the " + i + " th message");
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
