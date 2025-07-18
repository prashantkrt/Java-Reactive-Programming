package com.myLearning.part09_sinks;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class Part07DirectBestEffort {

    private static final Logger log = LoggerFactory.getLogger(Part07DirectBestEffort.class);

    /*
    | Use Case                                 | Best Choice                 |
    | ---------------------------------------- | --------------------------- |
    | Low latency, fast stream                 | .directBestEffort()         |
    | Guaranteed delivery (bounded buffer)     | .onBackpressureBuffer()     |
    | Replay history                           | .replay()                   |
    */
    public static void main(String[] args) {

        demo2();

        Util.sleepSeconds(10);
    }

    /*
      When we have multiple subscribers, if one subscriber is slow,
      we might not be able to safely deliver messages to all the subscribers /
      other fast subscribers might not get the messages.
   */
    private static void demo1(){

        System.setProperty("reactor.bufferSize.small", "16"); // reducing the buffer size from 256 to 16

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
        flux.subscribe(Util.getSubscriber("Subscriber 2"));

        // Slow consumer due to .delayElements(Duration.ofMillis(100)) → buffer fills up → overflow happens.
        // Multicast Sink only buffers for slow subscribers.
        // Subscriber 1 & 2 keep up =  no backpressure.
        // Subscriber 3 is delayed = messages get buffered up to 16 messages → after that → emits FAIL_OVERFLOW.
        flux.delayElements(Duration.ofMillis(100)).subscribe(Util.getSubscriber("Subscriber 2"));

        for (int i = 1; i <= 100 ; i++) {
            var result = sink.tryEmitNext(i);
            log.info("item: {}, result: {}", i, result);
        }

    }

    /*
        directBestEffort - focus on the fast subscriber and ignore the slow subscriber
     */
    private static void demo2(){

        System.setProperty("reactor.bufferSize.small", "16");

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue

        //Slow Subscriber Drops items when subscriber is slow
        // No guarantee that all subscribers get all messages for slower subscribers
        var sink = Sinks.many().multicast().directBestEffort(); //Drops immediately, no buffering, focuses on fastest delivery


        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
        // flux.delayElements(Duration.ofMillis(200)).subscribe(Util.getSubscriber("Subscriber 2"));

        // if we think sub 2 is slow put the backpressure so that it won't loose the data
        // it will take it from backpressue if its ready
        flux.onBackpressureBuffer().delayElements(Duration.ofMillis(200)).subscribe(Util.getSubscriber("Subscriber 2"));

        for (int i = 1; i <= 100 ; i++) {
            var result = sink.tryEmitNext(i);
            log.info("item : {}, result: {}", i, result);
        }

        // with this => flux.delayElements(Duration.ofMillis(200)).subscribe(Util.getSubscriber("Subscriber 2"));
        //Subscriber 1 is fast, so it consumes everything.
        //Subscriber 2 is slow (delayElements(200ms)), so:
        //First element (1) is delivered immediately.
        //Then sink emits faster than Subscriber 2 can process → subsequent elements are dropped.


        //add the back pressure
        // solution => flux.onBackpressureBuffer.delayElements(Duration.ofMillis(200)).subscribe(Util.getSubscriber("Subscriber 2"));

    }
}
