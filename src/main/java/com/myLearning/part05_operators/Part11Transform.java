package com.myLearning.part05_operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.function.Function;

// transform is used when you want to apply a reusable transformation logic to a Flux or Mono.
// When we have a reusable chain of operations and want to keep your code DRY (Don't Repeat Yourself).

//We pass a function that takes a Flux<T> or Mono<T> and returns a transformed one — like filtering, mapping, logging, etc.
public class Part11Transform {

    private static final Logger logger = LoggerFactory.getLogger(Part11Transform.class);

    public static void main(String[] args) {

        // Reusable transformation function

        // passing Flux<Integer> and returning Flux<String>
        Function<Flux<Integer>, Flux<String>> evenNumberToString = flux ->
                flux.filter(i -> i % 2 == 0)
                        .map(i -> "Even: " + i);

        // A Flux of numbers
        Flux<Integer> numbers = Flux.range(1, 6);

        // Apply the transformation using transform
        numbers
                .transform(evenNumberToString)
                .subscribe(System.out::println);


        // Define reusable transformation logic
        Function<Flux<String>, Flux<String>> processUsernames = flux ->
                flux.map(String::trim)
                        .filter(name -> !name.isBlank())
                        .map(name -> "User: " + name.toUpperCase());

        // Flux of raw usernames
        Flux<String> usernames = Flux.just(" alice ", "  ", "Bob", "   charlie", "");

        // Apply transform
        usernames
                .transform(processUsernames)
                .subscribe(System.out::println);


    }
}
