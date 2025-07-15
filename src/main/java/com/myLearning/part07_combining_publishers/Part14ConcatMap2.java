package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Part14ConcatMap2 {
    public static void main(String[] args) {

        // Create a list of orders (just represented by order IDs for simplicity)
        Flux.just(101, 102, 103)  // Flux of orders
                .concatMap(orderId -> validateOrder(orderId))  // Validate the order first
                .concatMap(orderId -> processPayment(orderId))  // Process payment after validation
                .concatMap(orderId -> shipOrder(orderId))  // Ship the order after payment
                .subscribe(result -> System.out.println(result));

        //Validating order: 101
        //Processing payment for order: 101
        //Validating order: 102
        //Shipping order: 101
        //101
        //Processing payment for order: 102
        //Validating order: 103
        //Shipping order: 102
        //102
        //Processing payment for order: 103
        //Shipping order: 103
        //103

        Util.sleepSeconds(10);

    }

    // Simulate order validation (returning orderId after validation)
    private static Mono<Integer> validateOrder(Integer orderId) {
        System.out.println("Validating order: " + orderId);
        return Mono.just(orderId)
                .delayElement(Duration.ofMillis(300));  // Simulating validation delay
    }

    // Simulate payment processing (returning orderId after payment)
    private static Mono<Integer> processPayment(Integer orderId) {
        System.out.println("Processing payment for order: " + orderId);
        return Mono.just(orderId)
                .delayElement(Duration.ofMillis(500));  // Simulating payment delay
    }

    // Simulate order shipping (returning orderId after shipping)
    private static Mono<Integer> shipOrder(Integer orderId) {
        System.out.println("Shipping order: " + orderId);
        return Mono.just(orderId)
                .delayElement(Duration.ofMillis(200));  // Simulating shipping delay
    }
}
