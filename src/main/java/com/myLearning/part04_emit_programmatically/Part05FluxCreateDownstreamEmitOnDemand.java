package com.myLearning.part04_emit_programmatically;

import com.myLearning.part01_intro.subscriber.SubscriberImpl;
import com.myLearning.part04_emit_programmatically.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class Part05FluxCreateDownstreamEmitOnDemand {
    private static final Logger logger = LoggerFactory.getLogger(Part05FluxCreateDownstreamEmitOnDemand.class);

    public static void main(String[] args) {

//        var subscriber1 = new SubscriberImpl();
//        //example of early emit it is default behavior
//        Flux.<String>create(fluxSink -> {
//            for (int i = 0; i < 100 ; i++) {
//                var name = Util.faker().name().firstName();
//                logger.info("Generated name: {}", name); // will be logged immediately even we have cancelled the subscription
//                fluxSink.next(name);
//            }
//            fluxSink.complete();
//        }).subscribe(subscriber1);
//
//        subscriber1.getSubscription().request(1);
//        subscriber1.getSubscription().cancel();


        var subscriber2 = new SubscriberImpl();
        //Produce items on demand
        Flux.<String>create(fluxSink -> {
            fluxSink.onRequest(n -> {
                for (int i = 0; i < n &&  !fluxSink.isCancelled(); i++) {
                    var name = Util.faker().name().firstName();
                    logger.info("Generated name : {}", name); // will be logged till we haven't cancelled the subscription
                    fluxSink.next(name);
                }
            });
        }).subscribe(subscriber2);

        subscriber2.getSubscription().request(2);
        subscriber2.getSubscription().request(2);
        subscriber2.getSubscription().request(1);
        subscriber2.getSubscription().request(1);
        subscriber2.getSubscription().cancel();
        subscriber2.getSubscription().request(3);
    }
}

/*
FYI
public interface FluxSink<T> {

    // Emit a single item
    FluxSink<T> next(T t);

    // Signal that the stream is completed
    void complete();

    // Signal an error
    void error(Throwable e);

    // Optional: Request how many items the subscriber wants
    FluxSink<T> onRequest(LongConsumer consumer);

    // Optional: Set a cancellation callback
    FluxSink<T> onCancel(Disposable d);

    // Optional: Set a callback for termination (completion or error)
    FluxSink<T> onDispose(Disposable d);

    // Get the current requested demand
    long requestedFromDownstream();

    // Optional: Get the current FluxSink overflow strategy
    OverflowStrategy getOverflowStrategy();
}
 */

