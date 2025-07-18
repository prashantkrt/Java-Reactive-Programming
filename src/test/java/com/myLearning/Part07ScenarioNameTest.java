package com.myLearning;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;

public class Part07ScenarioNameTest {
    private Flux<Integer> getItems() {
        return Flux.range(1, 3);
    }

    // let's say we have some failure
    // [1 to 3 items test] expectation "first item should be 1" failed (expected value: 9; actual value: 1)
    @Test
    public void scenarioNameTest() {
        var options = StepVerifierOptions.create().scenarioName("1 to 3 items test");
        StepVerifier.create(getItems(), options)
                .expectNext(1)
                .as("first item should be 1")
                .expectNext(2, 3)
                .as("then 2 and 3")
                .expectComplete()
                .verify();
    }


    // In case of failure
    // Expectation "first item should be 1" failed (expected value: 0; actual value: 1)
    @Test
    public void scenarioNameTest2() {
        StepVerifier.create(getItems())
                .expectNext(0)
                .as("first item should be 1")
                .expectNext(2, 3)
                .as("then 2 and 3")
                .expectComplete()
                .verify();
    }
}
