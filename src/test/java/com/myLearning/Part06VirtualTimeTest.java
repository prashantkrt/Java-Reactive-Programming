package com.myLearning;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Part06VirtualTimeTest {

    private Flux<Integer> getItems() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(10));
    }

    // @Test We can not run like this
    public void demo() {
        StepVerifier.create(getItems())
                .expectNext(1, 2, 3, 4, 5)
                .expectComplete()
                .verify();
    }


    @Test
    public void virtualTimeTest1() {
        StepVerifier.withVirtualTime(()->getItems())// takes supplier
                .thenAwait(Duration.ofSeconds(51))
                .expectNext(1, 2, 3, 4, 5)
                .expectComplete()
                .verify();
    }

    @Test
    public void virtualTimeTest2() {
        StepVerifier.withVirtualTime(()->getItems())// takes supplier
                .thenAwait(Duration.ofSeconds(30))
                .expectNext(1, 2, 3 )
                .thenAwait(Duration.ofSeconds(20))
                .expectNext(4, 5)
                .expectComplete()
                .verify();
    }

    @Test
    public void virtualTimeTest3() {
        StepVerifier.withVirtualTime(this::getItems)
                .expectSubscription() // expecting the subscription as because we have expected no event but when we use verify it will create a subscribe event and it will fail
                .expectNoEvent(Duration.ofSeconds(9))
                .thenAwait(Duration.ofSeconds(1))
                .expectNext(1)
                .thenAwait(Duration.ofSeconds(40))
                .expectNext(2, 3, 4, 5)
                .expectComplete()
                .verify();
    }
}
