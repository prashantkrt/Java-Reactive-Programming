package com.myLearning.part01_intro.publisher;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publisher
 * - subscribe
 *
 * public interface Publisher<T> {
 * void subscribe(Subscriber<? super T> subscriber);
 * }
 */

public class PublisherImpl implements Publisher<String> {

    private final Logger logger = LoggerFactory.getLogger(PublisherImpl.class);

    @Override
    public void subscribe(Subscriber<? super String> subscriber) {
        logger.info("subscribed to publisher");
        var subscription = new SubscriptionImpl(subscriber);
        subscriber.onSubscribe(subscription);
    }
}
