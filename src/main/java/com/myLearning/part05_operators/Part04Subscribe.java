package com.myLearning.part05_operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class Part04Subscribe {
    private static final Logger log = LoggerFactory.getLogger(Part04Subscribe.class);
    public static void main(String[] args) {
        //we can use this way as well
        Flux.range(1, 10)
                .doOnNext(i -> log.info("received: {}", i))
                .doOnComplete(() -> log.info("completed"))
                .doOnError(err -> log.error("error", err))
                .subscribe();

//        .subscribe(i-> System.out.println("Received: " + i),
//                err -> System.err.println("Error: " + err),
//                () -> System.out.println("Completed"));
    }
}
