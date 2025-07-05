package com.myLearning.part03_flux;

import com.myLearning.part03_flux.common.Util;
import reactor.core.publisher.Flux;

public class Part06FluxRange {
    public static void main(String[] args) {

        // return type Flux<Integer>
        // start and end is inclusive
        Flux<Integer> flux = Flux.range(1, 5); // emits 1 to 5

        flux.subscribe(
                i -> System.out.println("Received: " + i),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed")
        );

        Flux.range(1, 10)
                .filter(i -> i % 2 == 0)
                .map(i -> "Even: " + i)
                .subscribe(System.out::println);

        Flux.range(1, 10)
                .map(i -> Util.getFaker().name().firstName())
                .subscribe(Util.getSubscriber());
    }
}
