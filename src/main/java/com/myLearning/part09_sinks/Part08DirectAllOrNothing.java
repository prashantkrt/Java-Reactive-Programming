package com.myLearning.part09_sinks;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class Part08DirectAllOrNothing {

    private static final Logger log = LoggerFactory.getLogger(Part08DirectAllOrNothing.class);

    public static void main(String[] args) {

        demo1();

        Util.sleepSeconds(10);
    }

     /*
        directAllOrNothing - all or nothing - either deliver to all the subscribers or no one!
    */
    private static void demo1(){

        System.setProperty("reactor.bufferSize.small", "16");

        var sink = Sinks.many().multicast().directAllOrNothing();

        var flux = sink.asFlux();

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
        flux.delayElements(Duration.ofMillis(200)).subscribe(Util.getSubscriber("Subscriber 2"));

        for (int i = 1; i <= 100 ; i++) {
            var result = sink.tryEmitNext(i);
            log.info("item: {}, result: {}", i, result);
        }
    }
}
