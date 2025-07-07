package com.myLearning.part05_operators;

import com.myLearning.part02_mono.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Part10Timeout {

    private static final Logger logger = LoggerFactory.getLogger(Part10Timeout.class);

    public static void main(String[] args) {

        // If the value is not emitted within 2 seconds, it will throw an error
        // Operator called default onErrorDropped
        getName().timeout(Duration.ofSeconds(2))
                .subscribe(System.out::println);

        getName().timeout(Duration.ofSeconds(1),fallback())
                .subscribe(System.out::println);

        getName().timeout(Duration.ofSeconds(1)).onErrorReturn("Fallback")
                .subscribe(System.out::println);

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static Mono<String> getName() {
        return Mono.fromSupplier(() -> {
            logger.info("Generating value");
            return Util.getFaker().name().firstName();
        }).delayElement(Duration.ofSeconds(3));
    }


    private static Mono<String> fallback() {
        return Mono.fromSupplier(() -> "fallback-" + Util.getFaker().name().firstName())
                .delayElement(Duration.ofMillis(300))
                .doFirst(() -> logger.info("do first"));
    }
}
