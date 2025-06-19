package com.myLearning.part02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

/*
 * If we don't have any terminal operator, then stream operator will not execute
 * Java Streams are lazy by design — they don’t process data until a terminal operation is invoked.
 */

public class Part01LazyStream {

    private static final Logger logger = LoggerFactory.getLogger(Part01LazyStream.class);

    public static void main(String[] args) {

        Stream<String> stream = Stream.of("hello", "world");
        stream.forEach(System.out::println);

        List<Integer> list = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .peek(i -> logger.info("received : {}", i))
                .toList();
        System.out.println(list);
    }
}
