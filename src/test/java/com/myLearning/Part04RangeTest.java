package com.myLearning;

import com.myLearning.common.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Part04RangeTest {

    // producer 1
    private Flux<Integer> getItems() {
        return Flux.range(1, 50);
    }

    // producer 2
    private Flux<Integer> getRandomItems() {
        return Flux.range(1, 50)
                .map(i -> Util.faker().random().nextInt(1, 100));
    }


    // producer 1 test
    @Test
    public void rangeTest1() {
        StepVerifier.create(getItems())
                .expectNext(1, 2, 3) // 3 items + 47 items remaining
                .expectNextCount(47) // rest 47 items
                .expectComplete()
                .verify();
    }

    // producer 1 test
    @Test
    public void rangeTest2() {
        StepVerifier.create(getItems())
                .expectNext(1, 2, 3)// 3 items + 47 items remaining
                .expectNextCount(22) // 25 items remaining
                .expectNext(26, 27, 28) // 3 items + 22 items remaining
                .expectNextCount(22) // rest 22 items expected
                .expectComplete()
                .verify();
    }


    // producer 2 test
    @Test
    public void rangeTest3() {
        StepVerifier.create(getRandomItems())
                .expectNextMatches(i -> i > 0 && i < 101) // any random number between 1 and 100
                .expectNextMatches(i -> i > 0 && i < 101)
                .expectNextCount(48)// after validating 2 item 48 items remaining
                .expectComplete()
                .verify();
    }


    // producer 2 test
    @Test
    public void rangeTest4() {
        StepVerifier.create(getRandomItems())
                .thenConsumeWhile(i -> i > 0 && i < 101) // any random number between 1 and 100 for all the emitted items
                .expectComplete()
                .verify();

        // instead of always writing like   .expectNextMatches(i -> i > 0 && i < 101) ..
        //  .expectNextMatches(i -> i > 0 && i < 101)  .. till  49 times
        // we can use .thenConsumeWhile
    }

}

// we can also use
// verifyError() => expectError().verify();
// verifyError(RuntimeException.class) => expectError(RuntimeException.class).verify();
// verifyComplete() => expectComplete().verify();
