package com.myLearning.part06_hot_and_cold_publishers;

import com.myLearning.part06_hot_and_cold_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

// what if we wanted to see the past emitted data
/*
    - publish().autoConnect(0) will provide new values to the subscribers.
    - replay allows us to cache
 */
public class Part04PublisherCache {

    private static final Logger logger = LoggerFactory.getLogger(Part04PublisherCache.class);

    public static void main(String[] args) {

        // replay will cache the value and provide the data to the late comers
        // replay(10) for example will cache the last 10 values
        // replay() will cache the last values. This is equivalent to replay(Integer.MAX_VALUE).
        // replay()	Caches all emitted items (unbounded cache)
        // replay(int history)	Caches only the last history items
        // replay(Duration duration)	Caches items emitted within the given time window
        // replay(int history, Duration ttl)	Caches the last history items within a time window
        var stockFlux = stockStream().replay().autoConnect(0);

        Util.sleepSeconds(4);

        logger.info("Subscriber 1 joined after 4 seconds");
        stockFlux
                .take(4)
                .subscribe(Util.getSubscriber("Subscriber 1"));

        Util.sleepSeconds(3);

        logger.info("Subscriber 2 joined after 7 seconds");
        stockFlux
                .take(3)
                .subscribe(Util.getSubscriber("subscriber 2"));

        Util.sleepSeconds(40);


    }

    private static Flux<Integer> stockStream() {
        return Flux.<Integer>generate(sink -> sink.next(Util.faker().random().nextInt(10, 100)))
                .delayElements(Duration.ofSeconds(3))
                .doOnNext(price -> logger.info("emitting price: {}", price));
    }
}
