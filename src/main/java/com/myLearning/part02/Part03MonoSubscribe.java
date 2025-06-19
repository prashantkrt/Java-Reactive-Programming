package com.myLearning.part02;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Part03MonoSubscribe
{
    private static final Logger logger = LoggerFactory.getLogger(Part03MonoSubscribe.class);

    public static void main(String[] args) {

        // only subscribed will not see a completed message since its only subscribing not requesting
        // Emit the value "Hello" (i.e., onNext)
        // Automatically complete (i.e., onComplete is called internally)
        // But you're not printing "Completed", because you haven’t passed an onComplete handler.
        Mono.just("Hello").subscribe(System.out::println);

        // When the Mono finishes emitting (either after emitting 1 value or none), it calls onComplete().
        // If you supply a handler (like the lambda above), that handler executes your code, in this case:
        // System.out.println("Completed");
       Mono.just("Hello").subscribe(
                val -> System.out.println("Value: " + val),       // onNext
                err -> System.out.println("Error: " + err),       // onError
                () -> System.out.println("Completed")             //  onComplete <- This gets called
        );

        var mono = Mono.just(1);
        mono.subscribe(
                i -> logger.info("received: {}", i),
                err -> logger.error("error", err),
                () -> logger.info("completed!!"),
                subscription -> subscription.request(1)
        );

        var disposable = Mono.just("Hello World");
        disposable.subscribe(
                i -> logger.info("received data: {}", i),                  // onNext: Consumer<? super T>
                err -> logger.error("error", err),                    // onError: Consumer<? super Throwable>
                () -> logger.info("completed "),                               // onComplete: Runnable
                subscription -> subscription.request(1)          // onSubscribe: Consumer<? super Subscription>
        );

        // will not be emitting the value
        Mono.just(1).subscribe(
                System.out::println,
                err -> logger.error("error", err),
                () -> System.out.println("completed"),
                Subscription::cancel
        );
    }
}

/**
 * The Mono class provides various overloaded subscribe methods that accept functional
 * interface (FI) objects as parameters. These methods allow handling different events
 * in a reactive stream lifecycle.
 *
 *  Overloaded subscribe() methods in Mono:
 *
 * 1. Basic subscription (no handlers):
 *     public Disposable subscribe()
 *
 * 2. Handle only onNext event:
 *     public Disposable subscribe(Consumer<? super T> consumer)
 *
 * 3. Handle onNext and onError events:
 *     public Disposable subscribe(
 *         Consumer<? super T> consumer,
 *         Consumer<? super Throwable> errorConsumer
 *     )
 *
 * 4. Handle onNext, onError, and onComplete events:
 *     public Disposable subscribe(
 *         Consumer<? super T> consumer,
 *         Consumer<? super Throwable> errorConsumer,
 *         Runnable completeConsumer
 *     )
 *
 * 5. Handle all four events: onNext, onError, onComplete, and onSubscribe:
 *     public Disposable subscribe(
 *         @Nullable Consumer<? super T> consumer,
 *         @Nullable Consumer<? super Throwable> errorConsumer,
 *         @Nullable Runnable completeConsumer,
 *         @Nullable Consumer<? super Subscription> subscriptionConsumer
 *     )
 *
 *  Note:
 * The Mono class extends the Publisher interface, which defines the standard
 * reactive contract as per the Reactive Streams specification.
 *
 *  Publisher interface method:
 *
 *     public interface Publisher<T> {
 *         void subscribe(Subscriber<? super T> subscriber);
 *     }
 *
 * The Publisher interface expects a full Subscriber implementation to handle
 * all events. In contrast, Mono's overloaded subscribe methods offer a more
 * flexible and developer-friendly way using functional interfaces like:
 * - Consumer<T>
 * - Consumer<Throwable>
 * - Runnable
 * - Consumer<Subscription>
 */
