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
                .doFirst(() -> log.info("doFirst-1"))
                .doOnNext(i -> log.info("doOnNext"))
                .doOnSubscribe(subscription -> log.info("doOnSubscribe: {}", subscription))
                .doOnCancel(() -> log.info("doOnCancel"))
                .doOnError(error -> log.info("doOnError: {}", error.getMessage()))
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
    }
}
