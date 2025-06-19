package com.myLearning.part01.subscriber;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*    Subscriber
*  - onSubscribe
*  - onNext
*  - onError
*  - onComplete
*
*  public interface Subscriber<T> {
*      void onSubscribe(Subscription s);
*      void onNext(T t);
*      void onError(Throwable t);
*      void onComplete();
*  }
* */

public class SubscriberImpl implements Subscriber<String> {

    private final Logger logger = LoggerFactory.getLogger(SubscriberImpl.class);

    private Subscription subscription;

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onNext(String email) {
        logger.info("Received {}", email);
    }

    @Override
    public void onError(Throwable t) {
        logger.error("error occurred {}", String.valueOf(t));
    }

    @Override
    public void onComplete() {
        logger.info("completed!");
    }
}
