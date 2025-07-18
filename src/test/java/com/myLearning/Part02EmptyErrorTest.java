package com.myLearning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Part02EmptyErrorTest {

    Mono<String> getUsername(int userId) {
        return switch (userId) {
            case 1 -> Mono.just("user1");
            case 2 -> Mono.empty(); // null
            default -> Mono.error(new RuntimeException("invalid input"));
        };
    }

    @Test
    public void userTest() {
        StepVerifier.create(getUsername(1))
                .expectNext("user1")
                .expectComplete()
                .verify(); // subscribe
    }

    @Test
    public void emptyTest() {
        StepVerifier.create(getUsername(2))
                .expectNextCount(0)
                .expectComplete()
                .verify(); // subscribe
    }

    @Test
    void emptyTest2() {
        StepVerifier.create(getUsername(2))
                .expectComplete()
                .verify();
    }

    @Test
    public void errorTest1() {
        StepVerifier.create(getUsername(3))
                .expectError()
                .verify(); // subscribe
    }

    @Test
    public void errorTest2() {
        StepVerifier.create(getUsername(3))
                .expectError(RuntimeException.class)
                .verify(); // subscribe
    }

    @Test
    public void errorTest3() {
        StepVerifier.create(getUsername(3))
                .expectErrorMessage("invalid input")
                .verify(); // subscribe
    }

    @Test
    public void errorTest4() {
        StepVerifier.create(getUsername(3))
                .consumeErrorWith(err -> {
                    System.out.println("Error: " + err);
                    System.out.println("Error message: " + err.getMessage());
                    System.out.println("Error stack trace: " + err.getStackTrace());
                    Assertions.assertEquals(RuntimeException.class, err.getClass());
                    Assertions.assertEquals("invalid input", err.getMessage());
                })
                .verify();
    }

}

// we can also use
// verifyError() => expectError().verify();
// verifyError(RuntimeException.class) => expectError(RuntimeException.class).verify();
// verifyComplete() => expectComplete().verify();
