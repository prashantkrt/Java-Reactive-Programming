package com.myLearning;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Part09PublisherTest {

    // Example 1
    // Here we need producer as input now we have to use TestPublisher to fake the producer
    Flux<String> processData(Flux<String> input) {
        return input.map(String::toUpperCase);
    }

    // TestPublisher => Fake publisher for manual control in tests
    // TestPublisher => Simply to test the publisher with some useful methods like validations, asset expectations, min request values, etc.
    @Test
    void testWithTestPublisher() {
        // Step 1: Create TestPublisher
        TestPublisher<String> testPublisher = TestPublisher.create();

        // Step 2: Connect your method to this fake publisher
        Flux<String> processed = processData(testPublisher.flux());

        // Step 3: Use StepVerifier to subscribe & test
        StepVerifier.create(processed)
                .then(() -> testPublisher.emit("apple", "banana"))
                .expectNext("APPLE", "BANANA")
                .then(() -> testPublisher.complete())
                .verifyComplete();
    }


    //Example 2
    private UnaryOperator<Flux<String>> processor() {
        return flux -> flux
                .filter(s -> s.length() > 1)
                .map(String::toUpperCase)
                .map(s -> s + ":" + s.length());
    }

    // or
    private Function<Flux<String>, Flux<String>> processor2() {
        return flux -> flux
                .filter(s -> s.length() > 1)
                .map(String::toUpperCase)
                .map(s -> s + ":" + s.length());
    }


    @Test
    public void publisherTest1() {
        // TestPublisher<Object> objectTestPublisher = TestPublisher.create();
        // TestPublisher<String> stringTestPublisher = TestPublisher.<String>create();
        var testPublisher = TestPublisher.<String>create();
        var flux = testPublisher.flux(); // creating the fake publisher

        StepVerifier.create(flux.transform(processor()))
                .then(() -> testPublisher.emit("hi", "hello"))
                .expectNext("HI:2")
                .expectNext("HELLO:5")
                .expectComplete()
                .verify();
    }

}
