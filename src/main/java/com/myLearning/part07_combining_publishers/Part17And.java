package com.myLearning.part07_combining_publishers;


import reactor.core.publisher.Mono;

// .and() is a method in Reactor (Project Reactor) .and() returns a Mono<Void>.
// .and() always returns Mono<Void>, never Flux<Void>, no matter whether you chain it on Mono or Flux.
//  It ignores any emitted data (values) and only signals completion.
//  It is generally used when you want to run two publishers but ignore the value emitted and only care about the completion signal.
public class Part17And {
    public static void main(String[] args) {

        Mono<Void> first = Mono.fromRunnable(() -> System.out.println("First completed"));
        Mono<Void> second = Mono.fromRunnable(() -> System.out.println("Second completed"));

        first.and(second)
                .subscribe();

        // Output:
        // First completed
        // Second completed

        //Operator	Behavior
        //then()	Ignores the first value, returns the value of the second publisher.
        //and()	    Ignores both values and returns Mono<Void> after both complete.

        Mono<String> firstMono = Mono.just("First");
        Mono<String> secondMono = Mono.just("Second");

        firstMono.then(secondMono) // returns Mono<String> with "Second"
                .subscribe(System.out::println);

        firstMono.and(secondMono) // returns Mono<Void>
                .subscribe(v -> System.out.println("Both completed"));

        // Output:
        // Second
        // Both completed

    }
}
