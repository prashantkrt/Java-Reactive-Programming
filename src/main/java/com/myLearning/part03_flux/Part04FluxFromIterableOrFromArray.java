package com.myLearning.part03_flux;

import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class Part04FluxFromIterableOrFromArray {
    public static void main(String[] args) {

        //Creates a Flux<T> from any Java Iterable — like List, Set, Queue, etc.
        List<String> names = Arrays.asList("Prashant", "Kumar", "Tiwary");

        Flux<String> fluxFromIterable = Flux.fromIterable(names);

        fluxFromIterable.subscribe(
                name -> System.out.println("FromIterable: " + name),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed fromIterable")
        );

        //Creates a Flux<T> from a Java array.
        String[] fruits = {"Apple", "Banana", "Cherry"};

        Flux<String> fluxFromArray = Flux.fromArray(fruits);

        fluxFromArray.subscribe(
                fruit -> System.out.println("FromArray: " + fruit),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed fromArray")
        );

    }
}
