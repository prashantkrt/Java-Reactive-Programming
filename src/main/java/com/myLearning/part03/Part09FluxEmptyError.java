package com.myLearning.part03;

import reactor.core.publisher.Flux;

public class Part09FluxEmptyError {
    public static void main(String[] args) {

        //Creates a Flux that emits nothing and immediately completes.
        Flux<String> emptyFlux = Flux.empty();

        emptyFlux.subscribe(
                item -> System.out.println("Item: " + item),
                error -> System.out.println("Error: " + error),
                () -> System.out.println("Completed (empty)")
        );

        //Flux that immediately terminates with an error.
        Flux<String> errorFlux = Flux.error(new RuntimeException("Something went wrong"));

        errorFlux.subscribe(
                item -> System.out.println("Item: " + item),
                error -> System.out.println("Error: " + error.getMessage()),
                () -> System.out.println("Completed")
        );
    }
}
