import consumers.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
        var consumer = new Consumer("message-consumer-group", System.getenv("TOPIC"), log);

        consumer.poll();

    }
}
