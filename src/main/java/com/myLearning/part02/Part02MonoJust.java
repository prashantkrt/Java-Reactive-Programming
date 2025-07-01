package com.myLearning.part02;

import com.myLearning.part01.subscriber.SubscriberImpl;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Part02MonoJust {
    private static final Logger logger = LoggerFactory.getLogger(Part02MonoJust.class);
    public static void main(String[] args) {

        Publisher<String> publisher = Mono.just("Hello");
        System.out.println(publisher); //MonoJust object

        var subscriber = new SubscriberImpl(); //adding the subscriber for which publisher will send the subscription
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(5);

        // adding these will have no effect as producer already sent complete
        // as isCancelled is set to true for the previous request
        subscriber.getSubscription().request(5);
        subscriber.getSubscription().cancel();

        publisher.subscribe(new Subscriber(){
            @Override
            public void onSubscribe(Subscription subscription) {
              subscription.request(5);
            }

            @Override
            public void onNext(Object o) {
              logger.info("Received {}", o);
            }

            @Override
            public void onError(Throwable t) {
               logger.error(t.getMessage(), t);
            }

            @Override
            public void onComplete() {
                logger.info("Completed");
            }
        });


        Mono<String> mono = Mono.just("Hello");
        System.out.println(mono); // MonoJust

        mono.subscribe(System.out::println); // Subscribes and automatically requests; onComplete is called internally, but not printed since no onComplete handler is provided

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

