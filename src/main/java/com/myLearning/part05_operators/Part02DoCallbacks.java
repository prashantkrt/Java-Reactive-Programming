package com.myLearning.part05_operators;

import com.myLearning.part02_mono.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

public class Part02DoCallbacks {

    private static final Logger log = LoggerFactory.getLogger(Part02DoCallbacks.class);

    public static void main(String[] args) {

        Flux<Integer> flux = Flux.<Integer>create(fluxSink -> {
            log.info("producer begins");
            for (int i = 0; i < 4; i++) {
                fluxSink.next(i);
            }
            fluxSink.complete();
            log.info("producer ends");
        });

        flux.doOnComplete(() -> log.info("doOnComplete"))
                .doFirst(() -> log.info("doFirst"))
                .doOnNext(i -> log.info("doOnNext"))
                .doOnRequest(request -> log.info("doOnRequest: {}", request)) // On request Integer.MAX_VALUE)
                .doOnSubscribe(subscription -> log.info("doOnSubscribe: {}", subscription))
                .doOnCancel(() -> log.info("doOnCancel"))
                .doOnError(error -> log.info("doOnError: {}", error.getMessage()))
                .doOnTerminate(() -> log.info("doOnTerminate")) //This operator is invoked when the stream terminates, regardless of whether it completes successfully, errors out, or is cancelled.
                .doOnDiscard(Object.class, o -> log.info("doOnDiscard: {}", o)) // If any item is discarded due to backpressure, this operator is invoked. It is generally used when the stream is large, and not all emitted items are consumed.
                .doFinally(signal -> {
                    if (signal == SignalType.ON_COMPLETE) {
                        System.out.println("Stream completed successfully!");
                    } else if (signal == SignalType.ON_ERROR) {
                        System.out.println("Stream ended with an error.");
                    } else if (signal == SignalType.CANCEL) {
                        System.out.println("Stream was cancelled.");
                    }
                })
                .subscribe(Util.getSubscriber("subscriber"));

        System.out.println("-------------------------------------------");

        //example 2
        Flux.just(1, 2, 3, 4, 5)
                .doOnSubscribe(subscription -> log.info("doOnSubscribe-1: {}", subscription)) // When subscribed
                .doFirst(() -> log.info("doFirst-1")) // Before processing starts
                .doOnRequest(request -> log.info("doOnRequest-1: {}", request)) // On request 3 since we have requested 3
                .doOnNext(item -> log.info("doOnNext-1: {}", item)) // For each item emitted
                .doOnError(error -> log.info("doOnError-1: {}", error.getMessage())) // On error
                .doOnTerminate(() -> log.info("doOnTerminate-1")) // When stream ends (success/error/cancel)
                .doOnCancel(() -> log.info("doOnCancel-1")) // On cancel
                .doOnDiscard(Object.class, o -> log.info("doOnDiscard-1: {}", o)) // doOnDiscard only gets triggered when an item is emitted but not consumed,
                .doOnComplete(() -> log.info("doOnComplete-1")) // When stream completes successfully
                .doFinally(signal -> log.info("doFinally-1: {}", signal)) // Always executed on completion/error/cancel
                .take(3) // Take first 3 items
                .subscribe(
                        item -> log.info("Received: {}", item), // OnNext
                        error -> log.error("Error occurred: {}", error), // OnError
                        () -> log.info("Stream completed") // OnComplete
                );


        System.out.println("-------------------------------------------");

        //example 3
        Flux<Integer> numberStream = Flux.range(1, 10)
                .doOnDiscard(Object.class, o -> log.info("doOnDiscard-1: Discarded item: {}", o))  // Observe discarded items
                .doOnSubscribe(subscription -> log.info("doOnSubscribe-1: {}", subscription))
                .doOnRequest(request -> log.info("doOnRequest-1: {}", request))
                .doOnNext(item -> log.info("doOnNext-1: {}", item))
                .doOnComplete(() -> log.info("doOnComplete-1"))
                .doOnTerminate(() -> log.info("doOnTerminate-1"))
                .doFinally(signal -> log.info("doFinally-1: {}", signal));

        // Using take(5) will ensure that only 5 items are processed and the rest are discarded.
        numberStream
                .take(5) // Limit the number of items consumed
//                .subscribe(
//                        item -> log.info("Received: {}", item),  // OnNext
//                        error -> log.error("Error occurred due to: {}", error),  // OnError
//                        () -> log.info("Stream completed")  // OnComplete
//                );
                .subscribe(
                        item -> {
                            try {
                                Thread.sleep(1000);  // Slow subscriber (simulate backpressure)
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            log.info("Received: {}", item);  // OnNext
                        },
                        error -> log.error("Error occurred: {}", error),  // OnError
                        () -> log.info("Stream completed")  // OnComplete
                );
    }
}
