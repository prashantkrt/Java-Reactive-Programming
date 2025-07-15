package com.myLearning.part07_combining_publishers;

//concatMap:
//Purpose: It processes elements in the order they are emitted and ensures the results are emitted sequentially, meaning it waits for one operation to finish before starting the next.
//Use Case: It's used when you need to maintain the order of elements and want to process them one at a time, even if each operation is asynchronous.


//flatMap:
//Purpose: It processes elements asynchronously and in parallel. It doesn't guarantee the order of processing or emission.
//Use Case: It's used when the order doesn't matter, and you want to process items concurrently for efficiency.


import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

// Like in flatmap internally, it uses mergeWith so order doesn't matter
// and with concatMap it uses concatWith

public class Part13ConcatMap {
    public static void main(String[] args) {
        // Using concatMap to process three different producers sequentially
        Flux.just(Producer1(), Producer2(), Producer3())  // Creating the three producers as a list of Flux
                .concatMap(producer -> producer)  // Concatenate the items from each producer sequentially
                .concatMap(item -> processItem(item))  // Process each item sequentially
                .subscribe(result -> System.out.println("Processed: " + result));

        //Processed: Processed Producer1 Item 1
        //Processed: Processed Producer1 Item 2
        //Processed: Processed Producer1 Item 3
        //Processed: Processed Producer2 Item 1
        //Processed: Processed Producer2 Item 2
        //Processed: Processed Producer3 Item 1
        //Processed: Processed Producer3 Item 2
        //Processed: Processed Producer3 Item 3
        //Processed: Processed Producer3 Item 4


        Flux<Flux<String>> just = Flux.just(Producer1(), Producer2(), Producer3());
        Flux<String> stringFlux = just.concatMap(producer -> producer);
        stringFlux.subscribe(System.out::println);

        //Producer1 Item 1
        //Producer1 Item 2
        //Producer1 Item 3
        //Producer2 Item 1
        //Producer2 Item 2
        //Producer3 Item 1
        //Producer3 Item 2
        //Producer3 Item 3
        //Producer3 Item 4


        Util.sleepSeconds(5);
    }

    // Simulating a processing step for each item
    private static Mono<String> processItem(String item) {
        return Mono.just("Processed " + item)
                .delayElement(Duration.ofMillis(50)); // Simulate processing delay
    }

    // Producer 1: Emits a Flux of 3 items with a delay
    private static Flux<String> Producer1() {
        return Flux.just("Producer1 Item 1", "Producer1 Item 2", "Producer1 Item 3")
                .delayElements(Duration.ofMillis(100)); // Simulate delay
    }

    // Producer 2: Emits a Flux of 2 items with a delay
    private static Flux<String> Producer2() {
        return Flux.just("Producer2 Item 1", "Producer2 Item 2")
                .delayElements(Duration.ofMillis(150)); // Simulate delay
    }

    // Producer 3: Emits a Flux of 4 items with a delay
    private static Flux<String> Producer3() {
        return Flux.just("Producer3 Item 1", "Producer3 Item 2", "Producer3 Item 3", "Producer3 Item 4")
                .delayElements(Duration.ofMillis(200)); // Simulate delay
    }
}
