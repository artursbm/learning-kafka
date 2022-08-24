package events.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.slf4j.Logger;
import producers.KeylessProducer;

public class WikimediaEventHandler implements EventHandler {

    private KeylessProducer producer;
    private static Logger log;
    private static String topic;

    public WikimediaEventHandler(KeylessProducer producer, String topic, Logger logger) {
        this.producer = producer;
        this.topic = topic;
        this.log = logger;

    }

    @Override
    public void onOpen() {

    }

    @Override
    public void onClosed() {
        producer.finishProducer();
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        log.info(messageEvent.getData());
        producer.sendKeylessMessage(topic, messageEvent.getData());
    }

    @Override
    public void onComment(String comment) {

    }

    @Override
    public void onError(Throwable t) {

    }
}
