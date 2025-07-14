package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Flux;

/*
    Subscriber subscribes to all the producers at the same time.
    Project Reactor (Flux.merge) which combines multiple publishers and emits items as soon as they arrive, interleaving their values.
    Difference from concat(): concat() waits for one publisher to finish before subscribing to the next, while merge() runs them concurrently
 */
public class Part07MergeBasic {

    public static void main(String[] args) {

        example1();

    }

    private static void example1() {
        Flux<String> flux1 = Flux.just("Dog", "Cat");
        Flux<String> flux2 = Flux.just("Apple", "Banana");

        Flux.merge(flux1, flux2)
                .subscribe(System.out::println);

        //Since there’s no delay, it just emits sequentially but could be interleaved if async.
        //Dog
        //Cat
        //Apple
        //Banana

    }

    private static void example2() {
        Flux<String> flux1 = Flux.just("A", "B", "C")
                .delayElements(java.time.Duration.ofMillis(500));  // delays each element

        Flux<String> flux2 = Flux.just("1", "2", "3")
                .delayElements(java.time.Duration.ofMillis(300));  // faster delay

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        mergedFlux.subscribe(System.out::println);

        Util.sleepSeconds(10);

        //1
        //A
        //2
        //3
        //B
        //C
    }

}