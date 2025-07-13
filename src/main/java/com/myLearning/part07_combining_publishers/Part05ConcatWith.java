package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

// concatWith() is a method used to sequentially concatenate two publishers (like Flux or Mono)
// The first publisher emits all its items completely.
// Then, the second publisher starts emitting.
// concatWith() = sequential (first completes, then second starts)

public class Part05ConcatWith {

    private static final Logger logger = LoggerFactory.getLogger(Part05ConcatWith.class);

    public static void main(String[] args) {

        demo4();

        Util.sleepSeconds(5);
    }

    private static void demo1(){
        producer1() // first completes this Flux
                .concatWithValues(-1, 0) // then this Flux
                .subscribe(System.out::println);
    }

    private static void demo2(){
        producer1() // first completes this Flux
                .concatWith(producer2()) // then this Flux
                .subscribe(System.out::println);
    }

    private static void demo3(){
        Flux.concat(producer1(), producer2()) // first producer1 then producer2
                .subscribe(System.out::println);
    }

    private static void demo4(){
        producer1() // first completes this Flux
                .concatWith(producer2()) // then this Flux
                .concatWithValues(100, 200, 300) // then this Flux
                .subscribe(System.out::println);
    }


    private static Flux<Integer> producer1(){
        return Flux.just(1, 2, 3)
                .doOnSubscribe(s -> logger.info("subscribing to producer1"))
                .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer2(){
        return Flux.just(10, 20, 30)
                .doOnSubscribe(s -> logger.info("subscribing to producer2"))
                .delayElements(Duration.ofMillis(10));
    }
}
