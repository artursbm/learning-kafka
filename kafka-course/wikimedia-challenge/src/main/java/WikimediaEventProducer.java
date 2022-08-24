import com.launchdarkly.eventsource.EventSource;
import events.wikimedia.WikimediaEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import producers.KeylessProducer;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class WikimediaEventProducer {

    private static final Logger log = LoggerFactory.getLogger(WikimediaEventProducer.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {

        var producer = new KeylessProducer(log);

        var eventsTopic = "wikimedia.changes";

        var eventHandler = new WikimediaEventHandler(producer, eventsTopic, log);
        String sourceUrl = "https://stream.wikimedia.org/v2/stream/recentchange";

        var eventSource = new EventSource.Builder(eventHandler, URI.create(sourceUrl)).build();

        eventSource.start();

        TimeUnit.MINUTES.sleep(10);

    }

}
