package com.myLearning.part05_operators;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Part05ErrorHandling {

    private static final Logger log = LoggerFactory.getLogger(Part05ErrorHandling.class);

    public static void main(String[] args) {

        // on error return
        // Parameter: A single fallback value (T) to emit when any error occurs.
        // Returns: A new Flux<T> or Mono<T> that emits the fallback and completes.

        //example 1
        Flux.range(1, 10)
                .map(i -> i == 5 ? i / 0 : i)
                .onErrorReturn(0)
                .subscribe(i -> System.out.println("Received: " + i));


        System.out.println("------------------------------------");

        //example 2
        Flux<Integer> flux = Flux.range(1, 4)
                .map(i -> {
                    if (i == 3) {
                        throw new RuntimeException("Oops");
                    }
                    return i;
                })
                .onErrorReturn(999);

        flux.subscribe(System.out::println);

        System.out.println("************************************");

        // on error resume
        // A function that takes the Throwable (error) and returns a fallback Publisher (like Flux.just(...) or Mono.just(...)).
        // Returns: A new Flux<T> or Mono<T> that switches to the fallback sequence when an error occurs.

        /*
         * @FunctionalInterface
         * interface MyFunction<T, R> {
         *     R apply(T t); // exactly one abstract method
         * }
         */

        //example 1
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4)
                .map(i -> {
                    if (i == 3) throw new RuntimeException("Error at 3");
                    return i;
                })
                .onErrorResume(new Function<Throwable, Publisher<? extends Integer>>() {
                    @Override
                    public Publisher<? extends Integer> apply(Throwable throwable) {
                        System.out.println("Caught: " + throwable.getMessage());
                        return Flux.just(100, 200); // fallback stream
                    }
                });

        numbers.subscribe(
                data -> System.out.println("Received: " + data),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed")
        );

        System.out.println("------------------------------------");
        // example 2
        Flux.range(1, 10)
                .map(i -> i == 5 ? i / 0 : i)
                .onErrorResume(e -> {
                    System.out.println("Error handled: " + e.getMessage());
                    return Flux.just(100, 200); // continue with new fallback flux
                }).subscribe(i -> System.out.println("Received: " + i));

        System.out.println("************************************");


        // on error continue
        // Takes A BiConsumer that takes: with the Throwable (error) and value that caused the failure
        // Returns: A Flux<T> that skips the error and continues with the rest of the data.

        /*
         * @FunctionalInterface
         * public interface BiConsumer<T, U> {
         *     void accept(T t, U u);
         * }
         */

        // example 1
        Flux<Integer> nums = Flux.just(1, 2, 3, 4)
                .map(i -> {
                    if (i == 3) {
                        throw new RuntimeException("Error at 3");
                    }
                    return i;
                })
                .onErrorContinue(new BiConsumer<Throwable, Object>() {
                    @Override
                    public void accept(Throwable error, Object value) {
                        System.out.println("⚠️ Skipping value: " + value + " due to error: " + error.getMessage());
                    }
                });

        nums.subscribe(
                data -> System.out.println("Received: " + data),
                err -> System.out.println(" Error: " + err),
                () -> System.out.println("✔️ Stream completed")
        );

        System.out.println("------------------------------------");

        //example 2
        Flux.range(1, 10)
                .map(i -> i == 5 ? i / 0 : i)
                .onErrorContinue((error, item) -> {
                    System.out.println("Skipping bad value: " + item + " due to " + error.getMessage());
                }).subscribe(i -> System.out.println("Received: " + i));


        System.out.println("------------------------------------");


        //example 3
        Flux<Integer> fluxContinue = Flux.range(1, 6)
                .map(i -> {
                    if (i == 3 || i == 5) {
                        throw new RuntimeException("Bad data: " + i);
                    }
                    return i;
                })
                .onErrorContinue((error, item) -> {
                    System.out.println("Skipping bad value: " + item + " due to " + error.getMessage());
                });

        fluxContinue.subscribe(
                System.out::println,
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed")
        );


    }
}
