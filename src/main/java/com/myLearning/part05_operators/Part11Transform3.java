package com.myLearning.part05_operators;

import reactor.core.publisher.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Part11Transform3 {

    private static final Logger logger = LoggerFactory.getLogger(Part11Transform3.class);

    public static <T> Function<Flux<T>, Flux<T>> fluxLog(String name) {
        return new Function<Flux<T>, Flux<T>>() {
            @Override
            public Flux<T> apply(Flux<T> flux) {
                return flux
                        .doOnSubscribe(subscription -> logger.info("subscribing to {}", name))
                        .doOnCancel(() -> logger.info("cancelling {}", name))
                        .doOnComplete(() -> logger.info("{} completed", name));
            }
        };
    }

    // or
    public static <T> Function<Flux<T>,Flux<T>> fluxLogs(String name){
        return flux -> flux
                .doOnSubscribe(s -> logger.info("subscribing to {}", name))
                .doOnCancel(() -> logger.info("cancelling {}", name))
                .doOnComplete(() -> logger.info("{} completed", name));
    }

    // better use this since we are using the same input and return
    public static <T> UnaryOperator<Flux<T>> fluxLogger(String name){
        return flux -> flux
                .doOnSubscribe(s -> logger.info("subscribing to {}", name))
                .doOnCancel(() -> logger.info("cancelling {}", name))
                .doOnComplete(() -> logger.info("{} completed", name));
    }

    public static void main(String[] args) {
        Flux.range(1, 3)
                .transform(fluxLog("MyFlux"))
                .subscribe(System.out::println);
    }
}