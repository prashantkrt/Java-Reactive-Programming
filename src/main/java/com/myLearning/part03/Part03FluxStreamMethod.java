package com.myLearning.part03;

import com.myLearning.part03.common.Util;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Part03FluxStreamMethod {
    public static void main(String[] args) {

        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6);

        //1. .filter(Predicate<T>)
        //Return type: Flux<Integer>
        flux.filter(i -> i % 2 == 0)
                .subscribe(Util.getSubscriber("filter-even"));

        //2. .map(Function<T, R>)
        //Return type: Flux<R>
        flux.map(i -> i * 10)
                .subscribe(Util.getSubscriber("map-x10"));

        //3. .flatMap(Function<T, Publisher<R>>)
        //Return type: Flux<R>
        flux.flatMap(i -> Flux.just(i, i + 1))
                .subscribe(Util.getSubscriber("flatMap"));

        //example 2 with more clear code
        flux.flatMap(new Function<Integer, Publisher<Integer>>() {
            @Override
            public Publisher<Integer> apply(Integer i) {
                return Flux.just(i, i + 1); // returns Flux for each item
            }
        }).subscribe(
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) {
                        System.out.println("Received via flatMap: " + i); // onNext
                    }
                },
                new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        System.out.println("Error occurred: " + throwable.getMessage()); // onError
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Completed processing!"); // onComplete
                    }
                }
        );
        //for each element in flux that has 1, 2, 3, 4, 5, 6 we perform  Flux.just(i, i + 1);
        // this emits the number itself (i) and the next number (i + 1)
        // Original i	Emitted via Flux.just(i, i + 1)
        // 1	             1, 2
        // 2	             2, 3
        // 3	             3, 4
        // 4	             4, 5
        // 5                 5, 6
        // 6	             6, 7
        // so all these are then flattened into one stream via flatMap.
        //final output 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7


        //4. .take(n)
        //Return type: Flux<Integer>
        //Take the first n elements only.
        flux.take(3)
                .subscribe(Util.getSubscriber("take-3"));

        //5. .skip(n)
        //Return type: Flux<Integer>
        flux.skip(2)
                .subscribe(Util.getSubscriber("skip-2"));

        //or
        flux.skip(2)
                .subscribe(
                        i -> System.out.println("Skipped: " + i),
                        err -> System.err.println("Error: " + err),
                        () -> System.out.println("Completed skip")
                );

        //6.  doOnNext(...) just like peek()
        // Logging items before/after transformation
        // Debugging
        // Metrics collection
        // Monitoring pipeline
        //Return type: Flux<Integer> (no transformation)
        flux
                .doOnNext(i -> System.out.println("About to process: " + i))
                .map(i -> i * 2)
                .subscribe(i -> System.out.println("Received: " + i));
        //About to process: 1
        //Received: 2
        //About to process: 2
        //Received: 4
        //About to process: 3
        //Received: 6
        //About to process: 4
        //Received: 8
        //About to process: 5
        //Received: 10

        //7. .reduce((a, b) -> a + b)
        //Reduce to a single value (returns Mono).
        //Return type: Mono<Integer>
        Mono<Integer> mono = flux.reduce((a, b) -> a + b);
        mono.subscribe(Util.getSubscriber("reduce-sum"));


        //8. .collectList()
        //Collect all elements into a List<T> (returns Mono<List<T>>).
        //Return type: Mono<List<Integer>>
        flux.collectList()
                .subscribe(list -> System.out.println("Collected: " + list));

        // 9. distinct()
        //Return type: Flux<Integer>
        Flux<Integer> dupFlux = Flux.just(1, 2, 2, 3, 3, 4);
        dupFlux.distinct()
                .subscribe(Util.getSubscriber("distinct"));

        // 10. .sort()
        //Return type: Mono<List<Integer>>
        Mono<List<Integer>> sortedFlux = flux.collectSortedList();
        sortedFlux.subscribe(list -> System.out.println("Sorted List: " + list));
        //Sorted List: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

        //or better
        sortedFlux.subscribe(list -> list.forEach(System.out::println));


        //11 .delayElements(Duration)
        flux.delayElements(Duration.ofMillis(500))
                .subscribe(Util.getSubscriber("delayed"));


        //12  buffer(n)
        //.buffer(n) groups every n elements from the stream into a List<T>
        // So instead of emitting 1, 2, 3... separately, it emits: [1, 2], [3, 4], ...

        // for Flux<Integer> → after buffer(2) → it becomes Flux<List<Integer>>
        //return type: Flux<List<T>>
        flux.buffer(2)
                .subscribe(
                        list -> System.out.println("Buffered: " + list),
                        err -> System.err.println("Error: " + err),
                        () -> System.out.println("Completed buffer")
                );
        //Buffered: [1, 2]
        //Buffered: [3, 4]
        //Buffered: [5, 6]
        //Completed buffer
    }


}
