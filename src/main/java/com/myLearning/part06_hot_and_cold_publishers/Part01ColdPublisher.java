package com.myLearning.part06_hot_and_cold_publishers;

import com.myLearning.part04_emit_programmatically.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;
/*
*   Cold Publisher
* - Each subscriber gets its own data stream.
* - The data is re-emitted from the beginning for every new subscriber.
* - It starts emitting only when someone subscribes.
* */
public class Part01ColdPublisher {

    private static final Logger logger = LoggerFactory.getLogger(Part01ColdPublisher.class);

    public static void main(String[] args) {

        //example 1
        Flux<Integer> coldFlux = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("New Subscription"));

        coldFlux.subscribe(i -> System.out.println("Subscriber 1: " + i));
        coldFlux.subscribe(i -> System.out.println("Subscriber 2: " + i));

        //New Subscription
        //Subscriber 1: 1
        //Subscriber 1: 2
        //Subscriber 1: 3
        //New Subscription
        //Subscriber 2: 1
        //Subscriber 2: 2
        //Subscriber 2: 3


        // example 2
        AtomicInteger atomicInteger = new AtomicInteger(0);
        var flux = Flux.create(fluxSink -> {
            logger.info("invoked");
            for (int i = 0; i < 3; i++) {
                fluxSink.next("Index: "+i+" and value: "+atomicInteger.incrementAndGet());
            }
            fluxSink.complete();
        });


        // both the subscribers are independent
        flux.subscribe(Util.getSubscriber("sub1"));
        flux.subscribe(Util.getSubscriber("sub2"));

        // invoked
        // sub1 Received Index: 0 and value: 1
        // sub1 Received Index: 1 and value: 2
        // sub1 Received Index: 2 and value: 3
        // sub1 completed!
        // invoked
        // sub2 Received Index: 0 and value: 4
        // sub2 Received Index: 1 and value: 5
        // sub2 Received Index: 2 and value: 6
        // sub2 completed!
    }
}
