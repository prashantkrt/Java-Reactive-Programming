package com.myLearning;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/*
    To write a simple test using StepVerifier.
    StepVerifier acts like a subscriber.
    StepVerifier.create(publisher) example =>  StepVerifier.create(Mono<String> mono)

    StepVerifier is a utility in Project Reactor for testing reactive streams in Java.
    It allows developers to define and verify expectations for asynchronous sequences, ensuring they emit the correct values and handle errors appropriately.
 */
public class Part01MonoTest {

    public static final Logger logger = LoggerFactory.getLogger(Part01MonoTest.class);

    // assume this a method from your service class
    private Mono<String> getProduct(int id) {
        return Mono.fromSupplier(() -> "product-" + id)
                .doFirst(() -> logger.info("invoked"));
        //invoked
        //product-5
    }

    @Test
    public void productTest() {
        StepVerifier.create(getProduct(1)) //Start testing the behavior of your Mono. Hey StepVerifier, test this Mono, I expect certain values from it.This subscribes to the Mono internally.
                .expectNext("product-1")//Expect the next emitted item to be "product-1". You declare expectation → Mono must emit this value. If not, the test will fail.
                .expectComplete() //Expect the Mono to complete without error. You expect the stream to finish properly (no errors, no extra items).
                .verify(); // This is like pressing the “Play” button. It triggers the subscription, starts consuming events, and validates your expectations. Start the flow, actually run and verify everything
        // verify() is subscribing the publisher. this triggers everything
    }

}

// we can also use
// verifyError() => expectError().verify();
// verifyError(RuntimeException.class) => expectError(RuntimeException.class).verify();
// verifyComplete() => expectComplete().verify();

