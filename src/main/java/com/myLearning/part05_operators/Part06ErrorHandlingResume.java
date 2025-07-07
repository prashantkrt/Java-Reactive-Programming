package com.myLearning.part05_operators;

import reactor.core.publisher.Flux;

public class Part06ErrorHandlingResume {
    public static void main(String[] args) {
        Flux<String> productPrices = getPriceFromMainService()
                .onErrorResume(e -> {
                    System.out.println("Main service failed: " + e.getMessage());
                    return getPriceFromBackupService(); // fallback
                });

        productPrices.subscribe(
                price -> System.out.println("Price: " + price),
                err -> System.err.println("Final error: " + err),
                () -> System.out.println("Done")
        );
    }


    // Simulates the main price service (throws error)
    private static Flux<String> getPriceFromMainService() {
        return Flux.error(new RuntimeException("Main service unavailable"));
    }

    // Simulates a fallback backup price service
    private static Flux<String> getPriceFromBackupService() {
        return Flux.just("₹199", "₹299", "₹399");
    }
}