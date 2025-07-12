package com.myLearning.part06_hot_and_cold_publishers;


import com.myLearning.part06_hot_and_cold_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/*
    Real Hot Publisher behavior
    with publish().autoConnect(0)
    - starts emitting items immediately, regardless of whether there are subscribers.
    - does NOT stop when subscribers cancel. So it will start producing even for 0 subscribers once it started.
    - make it real hot - publish().autoConnect(0)
 */
public class Part03HotPublisherAutoConnect {

    private static final Logger logger = LoggerFactory.getLogger(Part03HotPublisherAutoConnect.class);

    public static void main(String[] args) {

        var movieFlux = movieStream().publish().autoConnect(0);

        Util.sleepSeconds(2);

        movieFlux
                .take(4)
                .subscribe(Util.getSubscriber("Subscriber 1"));

        Util.sleepSeconds(3);

        movieFlux
                .take(3)
                .subscribe(Util.getSubscriber("subscriber 2"));


        Util.sleepSeconds(15);

    }

    // movie theater
    private static Flux<String> movieStream() {
        return Flux.generate(
                        () -> {
                            logger.info("received the request");
                            return 1;
                        },
                        (state, sink) -> {
                            var scene = "movie scene " + state;
                            logger.info("playing {}", scene);
                            sink.next(scene);
                            return ++state;
                        }
                )
                .take(10)
                .delayElements(Duration.ofSeconds(1))
                .cast(String.class);
    }
}
