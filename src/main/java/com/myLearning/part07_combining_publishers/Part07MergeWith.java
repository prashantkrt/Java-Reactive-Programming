package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Part07MergeWith {

    private static final Logger logger = LoggerFactory.getLogger(Part07MergeWith.class);

    public static void main(String[] args) {
        demo2();
        Util.sleepSeconds(5);
    }

    private static void demo1() {
        Flux.merge(producer1(), producer2(), producer3())
                .subscribe(System.out::println);
    }

    private static void demo2() {
        producer2().mergeWith(producer1())
                .mergeWith(producer3())
                .take(2)
                .subscribe(System.out::println);
    }

    private static Flux<Integer> producer1() {
        return Flux.just(1, 2, 3)
                .transform(fluxLogger("producer1"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer2() {
        return Flux.just(10, 20, 30)
                .transform(fluxLogs("producer2"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer3() {
        return Flux.just(100, 200, 300)
                .transform(fluxLog("producer3"))
                .delayElements(Duration.ofMillis(10));
    }

    // just for revision purpose
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
    public static <T> Function<Flux<T>, Flux<T>> fluxLogs(String name) {
        return flux -> flux
                .doOnSubscribe(s -> logger.info("subscribing to {}", name))
                .doOnCancel(() -> logger.info("cancelling {}", name))
                .doOnComplete(() -> logger.info("{} completed", name));
    }

    // better
    public static <T> UnaryOperator<Flux<T>> fluxLogger(String name) {
        return flux -> flux
                .doOnSubscribe(s -> logger.info("subscribing to {}", name))
                .doOnCancel(() -> logger.info("cancelling {}", name))
                .doOnComplete(() -> logger.info("{} completed", name));
    }
}
