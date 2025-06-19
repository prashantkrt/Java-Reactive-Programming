package com.myLearning.part02;

import com.myLearning.part01.subscriber.SubscriberImpl;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class Part02MonoJust {
    public static void main(String[] args) {

        Publisher<String> publisher = Mono.just("Hello");
        System.out.println(publisher); //MonoJust

        var subscriber = new SubscriberImpl(); //adding the subscriber for which publisher will send the subscription
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(5);

        // adding these will have no effect as producer already sent complete
        // as isCancelled is set to true for the previous request
        subscriber.getSubscription().request(5);
        subscriber.getSubscription().cancel();


        Mono<String> mono = Mono.just("Hello");
        System.out.println(mono); // MonoJust

        mono.subscribe(System.out::println);
    }
}

/*
 * The Mono class provides various overloaded subscribe methods that accept functional
 * interface (FI) objects as parameters. These methods allow handling different events.
 *
 * public final class Mono<T> implements Publisher<T> {
 *     // Overloaded subscribe methods
 *     public Disposable subscribe(); // Basic, no handlers
 *     public Disposable subscribe(Consumer<? super T> consumer); // onNext only
 *     public Disposable subscribe(Consumer<? super T> consumer,
 *                                 Consumer<? super Throwable> errorConsumer); // onNext + onError
 *     public Disposable subscribe(Consumer<? super T> consumer,
 *                                 Consumer<? super Throwable> errorConsumer,
 *                                 Runnable completeConsumer); // onNext + onError + onComplete
 * }
 *
 * The Publisher interface, which is the superclass of Mono, provides a subscribe method
 * that takes a Subscriber as a parameter.
 *
 * public interface Publisher<T> {
 *     void subscribe(Subscriber<? super T> subscriber);
 * }
 */

