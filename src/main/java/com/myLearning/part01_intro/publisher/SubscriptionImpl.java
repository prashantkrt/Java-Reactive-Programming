package com.myLearning.part01_intro.publisher;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscription
 * - cancel
 * - request
 *
 * public interface Subscription {
 * void request(long n);   // for backpressure control
 * void cancel();          // to cancel subscription
 * }
 */

public class SubscriptionImpl implements Subscription {

    private static final int MAX_REQUESTED_ITEMS = 10;
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionImpl.class);
    private final Subscriber<? super String> subscriber;
    private final Faker faker;
    private boolean isCancelled;
    private int count=0;

    public SubscriptionImpl(Subscriber<? super String> subscriber) {
        this.subscriber = subscriber;
        this.faker = Faker.instance();
    }

    @Override
    public void request(long requested) {

        if (this.isCancelled) {
            return;
        }
        logger.info("Subscriber has requested " + requested + " items");

        if(requested > MAX_REQUESTED_ITEMS){
            this.subscriber.onError(new RuntimeException("Requested items cannot be more than " + MAX_REQUESTED_ITEMS));
            this.isCancelled = true;
            return;
        }

        for (int i = 0; i < requested && count < MAX_REQUESTED_ITEMS ; i++) {
            count++;
            this.subscriber.onNext(this.faker.internet().emailAddress());
        }

        if(count == MAX_REQUESTED_ITEMS){
            logger.info("no more data to send");
            this.subscriber.onComplete();
            this.isCancelled = true;
        }
    }

    @Override
    public void cancel() {
        logger.info("Subscription has been cancelled");
        this.isCancelled = true;
    }
}
