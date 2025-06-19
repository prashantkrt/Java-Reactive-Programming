package com.myLearning.part02.common;

import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;

public class Util {

    public static <T> Subscriber<T> getSubscriber() {
        return new DefaultSubscriber<>("");
    }
    public static <T> Subscriber<T> getSubscriber(String name) {
        return new DefaultSubscriber<>(name);
    }

    public static void main(String[] args) {
        var mono = Mono.just(1);
        mono.subscribe(getSubscriber());
        mono.subscribe(getSubscriber("subscriber1"));
        mono.subscribe(getSubscriber("subscriber2"));
        mono.subscribe(getSubscriber("subscriber3"));
    }
}
