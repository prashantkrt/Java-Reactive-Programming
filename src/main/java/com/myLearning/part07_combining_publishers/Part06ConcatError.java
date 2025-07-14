package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Part06ConcatError {

    private static final Logger log = LoggerFactory.getLogger(Part06ConcatError.class);

    public static void main(String[] args) {

        demo2();

        Util.sleepSeconds(10);
    }

    private static void demo1() {
        producer1()
                .concatWith(producer2())
                .concatWith(producer3()) // will fail
                .concatWith(Flux.just(40, 50, 60)) // will not run since it failed in producer3
                .subscribe(System.out::println);
    }

    private static void demo2() {
        // even if one publisher fails, all the other publishers will be executed
        Flux.concatDelayError(producer1(),producer3(),producer2() )
                .subscribe(System.out::println);
    }

    private static Flux<Integer> producer3() {
        return Flux.error(new RuntimeException("error in producer3"));
    }

    private static Flux<Integer> producer2() {
        return Flux.just(10, 20, 30)
                .doOnSubscribe(s -> log.info("subscribing to producer2"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer1() {
        return Flux.just(1, 2, 3)
                .doOnSubscribe(s -> log.info("subscribing to producer1"))
                .delayElements(Duration.ofMillis(10));
    }
}
