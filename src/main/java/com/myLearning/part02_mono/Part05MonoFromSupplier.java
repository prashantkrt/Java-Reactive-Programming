package com.myLearning.part02_mono;

import com.myLearning.part02_mono.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.List;

/*
 * To delay the execution using supplier / callable
 */

/*
 * public static <T> Mono<T> fromSupplier(Supplier<? extends T> supplier)
 * Mono.fromSupplier() creates a Mono<T> that:
 * - Does not execute the logic immediately.
 * - Defers execution until a subscription is made.
 * - Utilize a Supplier<T> functional interface from Java.
 *
 *  @FunctionalInterface
 *   public interface Supplier<T> {
 *    T get();  // no input, returns something
 *   }
 */
public class Part05MonoFromSupplier {

    private static final Logger logger = LoggerFactory.getLogger(Part05MonoFromSupplier.class);

    public static void main(String[] args) throws InterruptedException {

        Mono<String> mono = Mono.fromSupplier(() -> {
            System.out.println("Supplier is executing...");
            return "Hello world from lazy " + LocalTime.now();
        });
        System.out.println("Lazy Mono created"); // executes immediately


        //Immediately when Mono.just(...) is called (eager)
        var monoEager = Mono.just("Hello World from Eager" + LocalTime.now());
        System.out.println("Eager Mono created"); // executes immediately

        Thread.sleep(2000); // simulate dela

        mono.subscribe(value -> System.out.println("Lazy subscriber received: " + value));
        monoEager.subscribe(value -> System.out.println("Eager Subscriber received: " + value));

        //example 2
        List<Integer> list = List.of(1, 2, 3);
        //first let know about the problem with Mono.just()
        Mono<Integer> eagerMono = Mono.just(sum(list)); // will call the sum method eagerly

        //with Mono.fromSupplier() we can solve this
        Mono.fromSupplier(() -> sum(list)) // will call the sum when subscribed/needed
                .subscribe(Util.getSubscriber());
    }

    private static int sum(List<Integer> list) {
        logger.info("finding the sum of {}", list);
        return list.stream().mapToInt(a -> a).sum();
    }
}
