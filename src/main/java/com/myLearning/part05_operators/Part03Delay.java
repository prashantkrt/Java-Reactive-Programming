package com.myLearning.part05_operators;

import com.myLearning.part04_emit_programmatically.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Part03Delay {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .log()
                .delayElements(Duration.ofSeconds(1))
                .subscribe(Util.getSubscriber());

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
