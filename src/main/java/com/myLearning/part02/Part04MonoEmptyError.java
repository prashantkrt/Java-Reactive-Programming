package com.myLearning.part02;

import com.myLearning.part02.common.Util;
import reactor.core.publisher.Mono;

/**
 * Demonstrates usage of Mono.empty() and Mono.error() in Project Reactor.
 *
 * Mono.empty():
 * - Emits no value
 * - Immediately completes
 * - Does NOT call onNext or onError
 *
 * Mono.error(Throwable):
 * - Emits no value
 * - Immediately terminates with an error
 *
 * Mono.just(T):
 * - Emits a single value
 * - Then completes
 */

public class Part04MonoEmptyError {

    public static void main(String[] args) {

        getUsername(1).subscribe(Util.getSubscriber("subscriber1 - EMPTY"));      // should trigger onComplete only
        getUsername(2).subscribe(Util.getSubscriber("subscriber2 - ERROR"));      // should trigger onError
        getUsername(3).subscribe(Util.getSubscriber("subscriber3 - JUST"));       // should print value + onComplete
        getUsername(4).subscribe(Util.getSubscriber("subscriber4 - INVALID"));    // also error
        getUsername(2).subscribe(Util.getSubscriber("subscriber5 - ERROR again")); // repeated error

        // default onErrorDropped since we haven't provided an error handler
        getUsername(4).subscribe(
                val-> System.out.println("Value: " + val)     // onNext
        );

    }

    /**
     * Simulates fetching a username based on input.
     *
     * @param choice selector for Mono behavior
     * @return Mono<Object> representing different types of emissions
     */
    private static Mono<Object> getUsername(int choice) {
        return switch (choice) {
            case 1 -> Mono.empty(); // No data, only completion
            case 2 -> Mono.error(new RuntimeException("Random Error")); // Emits error
            case 3 -> Mono.just("His name is John Cena"); // Emits value and completes
            default -> Mono.error(new RuntimeException("Invalid input")); // Custom error
        };
    }
}
