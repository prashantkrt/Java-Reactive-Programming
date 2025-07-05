package com.myLearning.part03_flux;

import reactor.core.publisher.Flux;

import java.util.List;

public class Part07FluxLog {
    public static void main(String[] args) {

        Flux<Integer> flux = Flux.range(1, 3)
                .log() // logs everything internally
                .map(i -> i * 10);

        flux.subscribe(
                i -> System.out.println("Received: " + i),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Done!")
        );
        //[INFO] onSubscribe(FluxRange.RangeSubscription)
        //[INFO] request(unbounded)
        //[INFO] onNext(1)
        //Received: 10
        //[INFO] onNext(2)
        //Received: 20
        //[INFO] onNext(3)
        //Received: 30
        //[INFO] onComplete()
        //Done!

        Flux<String> fluxWithLog = Flux.just("apple", "banana", "cherry")
                .log("JUST-FLUX"); // log with custom name

        fluxWithLog.subscribe(
                fruit -> System.out.println("Received: " + fruit),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed!")
        );
        //[JUST-FLUX] | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
        //[JUST-FLUX] | request(unbounded)
        //[JUST-FLUX] | onNext(apple)
        //Received: apple
        //[JUST-FLUX] | onNext(banana)
        //Received: banana
        //[JUST-FLUX] | onNext(cherry)
        //Received: cherry
        //[JUST-FLUX] | onComplete()
        //Completed!


        List<String> items = List.of("A", "B", "C");
        Flux<String> fluxWithIterable = Flux.fromIterable(items)
                .log();

        flux.subscribe(i -> System.out.println("Item: " + i));
    }
}
