package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class Part02StartWith {

    private static final Logger logger = LoggerFactory.getLogger(Part02StartWith.class);

    public static void main(String[] args) {
        //demo1();
        //demo2();
        //demo3();
        demo4();
        Util.sleepSeconds(15);
    }

    // with values
    private static void demo1() {
        producer1()
                .startWith(-1,0)
                .subscribe(System.out::println);
    }

    // with iterable
    private static void demo2() {
        producer2()
                .startWith(List.of(-3,-2,-1,0))
                .subscribe(System.out::println);
    }

    // with publisher
    private static void demo3() {
        producer1()
                .startWith(producer2())
                .subscribe(System.out::println);
    }

    // order of execution from bottom to top
    // with multiple startWith calls
    private static void demo4() {
        producer2()
                .startWith(producer1()) // order 3
                .startWith(List.of(-3,-2,-1,0)) // order 2
                .startWith(-4,-5,-6) // order 1
                .subscribe(System.out::println);
    }

    private static Flux<Integer> producer1(){
        return Flux.just(1, 2, 3)
                .doOnSubscribe(s->logger.info("Subscribing to producer 1"))
                .delayElements(Duration.ofSeconds(1));
    }

    private static Flux<Integer> producer2(){
        return  Flux.just(10, 20, 30)
                .doOnSubscribe(s->logger.info("Subscribing to producer 2"))
                .delayElements(Duration.ofSeconds(1));
    }
}
