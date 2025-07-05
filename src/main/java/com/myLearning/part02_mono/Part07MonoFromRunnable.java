package com.myLearning.part02_mono;

import com.myLearning.part02_mono.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/*
 *
 *  Executes the Runnable when subscribed
 *  Does not emit any value (only completion signal)
 *  Returns a Mono<Void>
 *
 *  @FunctionalInterface
 *   public interface Runnable {
 *    void run(); // does not allow checked exceptions but can throw checked exceptions
 *   }
 *
 * */

/*
 * Lazy execution
 * */
public class Part07MonoFromRunnable {
    private final static Logger logger = LoggerFactory.getLogger(Part07MonoFromRunnable.class);

    public static void main(String[] args) {

        Runnable task = () -> {
            System.out.println("Running some task...");
            // e.g., logging, auditing, cleaning, etc.
        };

        Mono<Void> mono = Mono.fromRunnable(task);

        System.out.println("Mono created, now subscribing...");

        mono.subscribe(
                val -> System.out.println("onNext: " + val),   // won't be called since runnable doesn't return any value
                err -> System.err.println("onError: " + err),
                () -> System.out.println("onComplete")
        );

    }

    private static Mono<String> getProductName(int productId) {
        if (productId == 1) {
            return Mono.fromSupplier(() -> Util.getFaker().commerce().productName());
        }
        // return Mono.empty();// instead we can use fromRunnable since it doesn't return any value but can call other methods for specific message
        return Mono.fromRunnable(() -> notifyBusiness(productId));
    }

    private static void notifyBusiness(int productId) {
        logger.info("notifying business on unavailable product {}", productId);
    }

}
