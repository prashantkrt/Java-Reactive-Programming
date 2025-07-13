package com.myLearning.part07_combining_publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

//Flux<T> startWith(T... values)
//Flux<T> startWith(Iterable<? extends T> iterable)
//Flux<T> startWith(Publisher<? extends T> other)

public class Part01BasicStartWith {

    private static final Logger logger = LoggerFactory.getLogger(Part01BasicStartWith.class);

    public static void main(String[] args) {

        example1();
        System.out.println("*******************************");
        example2();
        System.out.println("*******************************");
        example3();
    }

    //Flux<T> startWith(T... values)
    private static void example1() {

        Flux<String> names = Flux.just("Ram", "Shyam", "Radha");

        Flux<String> namesWithStart = names.startWith("Hello", "User");

        namesWithStart.subscribe(System.out::println);
        //Hello
        //User
        //Ram
        //Shyam
        //Radha
    }


    //Flux<T> startWith(Iterable<? extends T> iterable)
    private static void example2() {

        Flux<Integer> numbers = Flux.just(4, 5, 6);
        List<Integer> prefix = List.of(1, 2, 3);

        numbers.startWith(prefix)
                .subscribe(System.out::println);

        //1
        //2
        //3
        //4
        //5
        //6
    }

    //Flux<T> startWith(Publisher<? extends T> other)
    private static void example3() {
        Flux<String> main = Flux.just("C", "D");
        Flux<String> prefix = Flux.just("A", "B");

        Flux<String> combined = main.startWith(prefix);

        combined.subscribe(System.out::println);
        //A
        //B
        //C
        //D
    }
}
