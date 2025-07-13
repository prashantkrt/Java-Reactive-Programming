package com.myLearning.part07_combining_publishers;

import reactor.core.publisher.Flux;

// Flux<T> concatWith(Publisher<? extends T> other)
public class Part04ConcatWithBasic {
    public static void main(String[] args) {

        Flux<String> flux1 = Flux.just("A", "B");
        Flux<String> flux2 = Flux.just("C", "D");

        flux1.concatWith(flux2)
                .subscribe(System.out::println);

    }
}
