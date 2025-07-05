package com.myLearning.part04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class Part07FluxGenerate {
    private static final Logger logger = LoggerFactory.getLogger(Part07FluxGenerate.class);

    public static void main(String[] args) {

//   Flux.generate method lazily produces the next value. The values are generated and emitted on demand

//        ==> Will keep generating values <==
//        Flux.generate(synchronousSink ->
//                synchronousSink.next(1)
//        ).subscribe(System.out::println);

//        ==> one time only we can perform the next operation <==
//        Flux.generate(synchronousSink -> {
//                    synchronousSink.next(1);
//                    synchronousSink.next(2); // will not allow
//                    synchronousSink.complete();
//                }
//        ).subscribe(System.out::println);


        // Like we did earlier we were performing the loop on create method
        // but with generate the loop is performed on demand from the publisher itself

//         lets say
//         Flux.generate(synchronousSink ->
//                synchronousSink.next(1)
//        ).subscribe(System.out::println);   it will keep generating values in an infinite loop

        // Use AtomicInteger for a mutable, thread-safe state
        AtomicInteger number = new AtomicInteger(1);

        Flux.generate(synchronousSink -> {
                    if (number.get() <= 5) {
                        synchronousSink.next(number.get());
                        number.incrementAndGet(); // Increment the state for the next call
                    } else {
                        synchronousSink.complete(); // Complete after generating 5 items
                    }
                }).subscribe(System.out::println); // Subscribe to print the generated values


        final int[] state = {1}; // Array is used to maintain mutable state
        Flux.generate(synchronousSink ->
                {
                    if (state[0] <= 5) {
                        synchronousSink.next(state[0]);
                        state[0]++; // Increment the state for the next call
                    } else {
                        synchronousSink.complete(); // Complete after generating 5 items
                    }
                }).subscribe(System.out::println);


        // Let's simulate fetching paged data from a database
        AtomicInteger page = new AtomicInteger(1);

        Flux.generate(sink -> {
                    // Simulate fetching data from the database by page
                    System.out.println("Fetching page " + page.get());
                    if (page.get() <= 3) {  // Only 3 pages in total
                        sink.next("Data from page " + page.get());
                        page.incrementAndGet(); // Move to the next page
                    } else {
                        sink.complete(); // No more pages to fetch
                    }
                }).subscribe(System.out::println); // Lazy fetching happens only when subscribed
        // Fetching page 1
        // Data from page 1
        // Fetching page 2
        // Data from page 2
        // Fetching page 3
        // Data from page 3
    }
}
