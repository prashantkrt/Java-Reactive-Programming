package com.myLearning.part07_combining_publishers;

import reactor.core.publisher.Flux;

//zip() combines multiple publishers (Flux or Mono) by pairing the elements together and applying a combining function (or just returning a Tuple)
// zip() combines first element of flux1 (A) with first element of flux2 (1), then second with second and so on.
// Zipping stops when any publisher runs out of elements.

// *** Zipping stops when any publisher runs out of elements. ***
// unlike with merge it takes all the elements from all the publishers  and donot stop until every publisher finishes
// zip() = parallel and will stop when any publisher finishes
public class Part09ZipBasic {
    public static void main(String[] args) {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<Integer> flux2 = Flux.just(1, 2, 3);

        Flux<String> zippedFlux = Flux.zip(flux1, flux2, (letter, number) -> letter + number);

        zippedFlux.subscribe(System.out::println);
        //A1
        //B2
        //C3
    }
}
