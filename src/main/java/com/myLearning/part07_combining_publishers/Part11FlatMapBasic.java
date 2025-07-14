package com.myLearning.part07_combining_publishers;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// flatMap is used to transform the elements emitted by a Mono or Flux into other Mono or Flux objects.
// It’s used to flatten nested Mono or Flux types.
public class Part11FlatMapBasic {

    public static void main(String[] args) {

//        monoToMono().subscribe(System.out::println);
//        fluxToFlux().subscribe(System.out::println);
//        monoToFlux().subscribe(System.out::println);
        fluxToMono().subscribe(System.out::println);

    }

    private static Mono<String> monoToMono() {
        Mono<Integer> initialMono = Mono.just(5);

        Mono<String> transformedMono = initialMono.flatMap(number -> {
            // Perform some transformation
            return Mono.just("The transformed number is: " + (number * 2));
        });

        return transformedMono;
    }

    private static Flux<Integer> fluxToFlux() {
        Flux<Integer> numbersFlux = Flux.just(1, 2, 3, 4);

        Flux<Integer> doubledNumbersFlux = numbersFlux.flatMap(number -> {
            return Flux.just(number * 2);  // Transform each number into its double
        });

        return doubledNumbersFlux;
    }

    private static void fluxToFlux2() {
        // Flux of integers that will be transformed
        Flux<Integer> numbersFlux = Flux.just(1, 2, 3);

        // Use flatMap to transform each integer into a Flux of strings
        Flux<String> resultFlux = numbersFlux.flatMap(number -> {
            // Each number is transformed into a Flux emitting strings
            return Flux.just("Value " + number + "A", "Value " + number + "B");
        });

        // Subscribe to the resultFlux and print the emitted values
        resultFlux.subscribe(System.out::println);
        //Value 1A
        //Value 1B
        //Value 2A
        //Value 2B
        //Value 3A
        //Value 3B
    }

    private static Flux<String> monoToFlux() {
        Mono<String> mono = Mono.just("Hello");

        Flux<String> flux = mono.flatMapMany(value -> Flux.just(value, value + " World", value + " Reactor"));

        return flux;
    }

    private static Mono<String> fluxToMono() {
        Flux<String> flux = Flux.just("Hello", "World", "Reactor");

        Mono<String> mono = flux.flatMap(value -> Mono.just(value.toUpperCase()))
                .reduce((first, second) -> first + " " + second);

        return mono;
    }
}
