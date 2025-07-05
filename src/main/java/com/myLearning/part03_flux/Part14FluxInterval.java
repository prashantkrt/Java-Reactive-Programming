package com.myLearning.part03_flux;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Part14FluxInterval {
    public static void main(String[] args) {
        // Create a Flux that emits a number (starting from 0) every 1 second
        Flux<Long> intervalFlux = Flux.interval(Duration.ofSeconds(1));

        // Subscribe to the Flux and print each emitted value
        intervalFlux
                .take(5) // Limit to first 5 values (0 to 4)
                .doOnNext(val -> System.out.println("Received: " + val)) // Log emitted value
                .doOnComplete(() -> System.out.println("All items emitted. Flux completed.")) // Log on complete
                .subscribe();

        // Keep the main thread alive so the Flux can emit values
        try {
            Thread.sleep(6000); // 6 seconds > 5 emissions at 1-second interval
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("*******************************");

        // Retry with Delay Simulation
        Flux.interval(Duration.ofSeconds(2))
                .take(3) // Retry 3 times
                .doOnNext(attempt -> System.out.println("Attempt #" + (attempt + 1) + ": Retrying..."))
                .doOnComplete(() -> System.out.println("Retry attempts finished."))
                .subscribe();

        try {
            Thread.sleep(7000); // Let it run
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("*******************************");

        //countdown
        int seconds = 5;

        Flux.interval(Duration.ofSeconds(1))
                .map(i -> seconds - i) // Count down: 5, 4, 3, ...
                .take(seconds + 1)
                .doOnNext(i -> System.out.println("Time left: " + i + " seconds"))
                .doOnComplete(() -> System.out.println("Time's up!"))
                .subscribe();

        try {
            Thread.sleep((seconds + 1) * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
