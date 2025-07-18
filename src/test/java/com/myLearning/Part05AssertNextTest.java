package com.myLearning;

import com.myLearning.common.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static junit.framework.Assert.assertTrue;

// .assertNext() allows you to write custom logic/assertions on the next emitted item using a lambda.
// Ideal when you need to do multiple checks on the emitted item
public class Part05AssertNextTest {

    //producer 1
    Mono<String> getUsername() {
        return Mono.just("user1");
    }

    // producer 2
    record Book(int id, String author, String title) {
    }

    private Flux<Book> getBooks() {
        return Flux.range(1, 3)
                .map(i -> new Book(i, Util.faker().book().author(), Util.faker().book().title()));
    }


    // producer 1 test
    @Test
    void testAssertNext1() {
        StepVerifier.create(getUsername())
                .assertNext(username -> {
                    System.out.println("Received: " + username);
                    assert username.startsWith("user");
                    assert username.length() == 5;
                })
                .verifyComplete();
    }

    @Test
    void testAssertNext2() {
        StepVerifier.create(getUsername())
                .assertNext(username -> {
                    assertTrue(username.startsWith("user"));
                })
                .verifyComplete();
    }


    // producer 2 test
    @Test
    public void assertNextTest() {
        StepVerifier.create(getBooks())
                .assertNext(b -> Assertions.assertEquals(1, b.id()))
                .thenConsumeWhile(b -> Objects.nonNull(b.title())) // .thenConsumeWhile() → Flux (multiple items) will check if any null for book title
                .expectComplete()
                .verify();
    }

    @Test
    public void collectAllAndTest() {
        StepVerifier.create(getBooks().collectList()) // List<Book>
                .assertNext(list -> Assertions.assertEquals(3, list.size()))
                .expectComplete()
                .verify();
    }

}

// we can also use
// verifyError() => expectError().verify();
// verifyError(RuntimeException.class) => expectError(RuntimeException.class).verify();
// verifyComplete() => expectComplete().verify();
