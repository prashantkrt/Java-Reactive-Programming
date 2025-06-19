package com.myLearning.part02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.LocalTime;

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

    }
}
