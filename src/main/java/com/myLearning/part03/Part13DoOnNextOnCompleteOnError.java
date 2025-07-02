package com.myLearning.part03;

import reactor.core.publisher.Flux;

public class Part13DoOnNextOnCompleteOnError {
    public static void main(String[] args) {
        // These are side effect operators
        // They don’t modify the data, but allow you to hook into the stream to perform actions (like logging, auditing, debugging).

        // Triggered every time a value is emitted from the Flux.
        // Logging emitted items
        // Debugging values
        // Monitoring data flow
        Flux.just("Java", "Spring", "Reactor")
                .doOnNext(item -> System.out.println("Emitted: " + item))
                .subscribe();

        // Called once, after the Flux emits all elements successfully and completes.
        // Logging successful stream completion
        // Triggering cleanup actions
        // Sending final status (e.g., success notification)
        Flux.just(1, 2, 3)
                .doOnNext(i -> System.out.println("Processing: " + i))
                .doOnComplete(() -> System.out.println("All items processed successfully."))
                .subscribe();


        //With Flux.error (notice doOnComplete not called)
        Flux.range(1, 3)
                .map(i -> {
                    if (i == 2) throw new RuntimeException("Here we go Boom..Boom..!");
                    return i;
                })
                .doOnNext(i -> System.out.println("Emitting: " + i))
                .doOnComplete(() -> System.out.println("Should not be printed"))
                .doOnError(e -> System.out.println("Error occurred: " + e.getMessage()))
                .subscribe();
    }
}
