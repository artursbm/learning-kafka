import consumers.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {

        var consumer = new Consumer("message-consumer-app", System.getenv("TOPIC"), log);

        // start the consumer in a new thread, to make it available for graceful shutdown
        final var mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown requested, let's exit by calling consumer wakeup...");
            consumer.wakeup();

            try {
                mainThread.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }));


        consumer.poll();

    }
}
