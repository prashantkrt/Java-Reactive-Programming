package com.myLearning.part02;

import com.myLearning.part02.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

/*
*  Takes supplier of mono
*  public static <T> Mono<T> defer(Supplier<? extends Mono<? extends T>> supplier)
*  Returns the fresh data on each subscription
*  To delay the creation of the publisher
*  Creation of publisher is deferred
*  Note: Creation of publisher is very lightweight, but even we wanted to defer creation of publisher, it will be created only when subscribed
* */
public class Part09MonoDefer {
    private static final Logger logger = LoggerFactory.getLogger(Part09MonoDefer.class);

    public static void main(String[] args) {

        // example 1
        // The method getName() is called immediately, during declaration.
        Mono<String> mono1 = Mono.just(getName()); // getName() is called now


        // example 2 with defer
        // The method getName() is not called until someone subscribes to the Mono.
        Mono<String> mono2 = Mono.defer(() -> Mono.just(getName())); // getName() is deferred

        // now will be created and called
        mono2.subscribe(Util.getSubscriber());

        // example 3 deferring + new value everytime when subscribed
        Mono<String> mono = Mono.defer(() -> {
            System.out.println("Generating value");
            return Mono.just("Hello at " + System.currentTimeMillis());
        });

        mono.subscribe(System.out::println); // Output: Generating value + Hello at <timestamp>
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        mono.subscribe(System.out::println); // Output: Generating value + Hello at <new timestamp>


        //example 4
        Mono<String> monoA = Mono.defer(() -> {
            return Mono.just(Util.getFaker().name().firstName());
        });

        Mono<String> monoB = Mono.defer(() -> {
           return  Mono.just(Util.getFaker().name().firstName());
        });

        Mono<String> monoC = Mono.defer(() -> {
            return  Mono.just(Util.getFaker().name().firstName());
        });

        monoA.subscribe((i)-> System.out.println(i),
                err -> System.out.println(err),
                () -> System.out.println("Completed"));

        System.out.println("*******************************----");

        monoB.subscribe(Util.getSubscriber("B"));
        System.out.println("*******************************----");

        monoC.subscribe(System.out::println,
                System.out::println,
                ()-> System.out.println("Completed"),
                subscription -> subscription.request(1));
        System.out.println("*******************************");

        // example 5
        Mono.defer(()-> createPublisher())
                .subscribe(Util.getSubscriber("subscriber1"));

    }

    private static String getName() {
        logger.info("getting name");
        return Util.getFaker().name().firstName();
    }


    // time-consuming publisher creation
    private static Mono<Integer> createPublisher(){
        logger.info("creating publisher");
        var list = List.of(1, 2, 3);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Mono.fromSupplier(() -> sum(list));
    }

    // time-consuming business logic
    private static int sum(List<Integer> list) {
        logger.info("finding the sum of {}", list);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list.stream().mapToInt(a -> a).sum();
    }
}
