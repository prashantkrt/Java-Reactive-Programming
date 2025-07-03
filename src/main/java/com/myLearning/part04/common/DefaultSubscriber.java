package com.myLearning.part04.common;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSubscriber<T> implements Subscriber<T> {

    private final Logger logger = LoggerFactory.getLogger(DefaultSubscriber.class);
    private final String name;

    public DefaultSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
       subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T item) {
        logger.info("{} Received {}",name, item);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("{}  error occurred {}", name, String.valueOf(throwable));
    }

    @Override
    public void onComplete() {
        logger.info("{} completed!",name);
    }
}
