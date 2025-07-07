package com.myLearning.part05_operators;


import reactor.core.publisher.Flux;

/*
    Similar to error handling.
    Handling empty!
 */
public class Part08DefaultIfEmpty {
    public static void main(String[] args) {

        Flux.range(1, 10)
                .filter(i -> i > 11)
                .defaultIfEmpty(50) // if there is no data emitted from the producer flux, then instead emit 50
                .subscribe(i -> System.out.println(i),
                        err -> System.out.println(err.getMessage()),
                        () -> System.out.println("Completed"));


    }
}
