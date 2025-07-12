package com.myLearning.part06_hot_and_cold_publishers;

import com.myLearning.part06_hot_and_cold_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/*
 *   Hot Publisher
 * - Starts emitting items immediately, regardless of whether there are subscribers.
 * - Subscribers share the same data stream.
 * - Late subscribers miss previous items.
 *
 * */
public class Part02HotPublisher {

    private static final Logger logger = LoggerFactory.getLogger(Part02HotPublisher.class);

    public static void main(String[] args) {


        // using share method to make the publisher hot
        // now the publisher will share the data stream, and now it won't be two separate streams
        // var movieFlux = movieStream().publish().refCount(1); // need at least one subscriber to start emitting items
        // share => publish().refCount(1)
        // It needs 1 min subscriber to emit data.
        // It stops when there is 0 subscriber.
        // Re-subscription - It starts again where there is a new subscriber.
        // To have min 2 subscribers, use publish().refCount(2);
        var movieFlux = movieStream().share(); // same as .publish().refCount(1);

        try {
            logger.info("Waiting for 2 seconds");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        movieFlux.subscribe(Util.getSubscriber("Subscriber 1"));

        try {
            logger.info("Waiting for 3 seconds");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // this will subscribe to the same data stream with 3 items can be anywhere between 1 - 10 movie scenes
       movieFlux.take(3).subscribe(Util.getSubscriber("Subscriber 2"));

        try {
            logger.info("Waiting for 15 seconds");
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static Flux<String> movieStream() {
        return Flux.generate(
                        () -> {
                            return 1;
                        },
                        (state, sink) -> {
                            var scene = "movie scene " + state;
                            logger.info("playing {}", scene);
                            sink.next(scene);
                            if (state == 10) {
                                sink.complete();
                            }
                            return ++state;
                        })
                .take(10)
                .delayElements(Duration.ofSeconds(1))
                .cast(String.class);
    }
}
