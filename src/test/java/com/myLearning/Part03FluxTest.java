package com.myLearning;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Part03FluxTest {

    private Flux<Integer> getItems() {
        return Flux.just(1, 2, 3)
                .log();
    }

    @Test
    public void fluxTest1() {
        StepVerifier.create(getItems())
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxTest2() {
        StepVerifier.create(getItems())
                .expectNext(1, 2, 3)
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxTest3() {
        StepVerifier.create(getItems(), 1)//  it will be requesting for just 1 item
                .expectNext(1)
                .thenCancel()
                .verify();
    }
}

// we can also use
// verifyError() => expectError().verify();
// verifyError(RuntimeException.class) => expectError(RuntimeException.class).verify();
// verifyComplete() => expectComplete().verify();

