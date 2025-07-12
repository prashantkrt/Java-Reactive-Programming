package com.myLearning.part06_hot_and_cold_publishers.common;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Mono;

public class Util {

    public static <T> Subscriber<T> getSubscriber() {
        return new DefaultSubscriber<>("");
    }
    public static <T> Subscriber<T> getSubscriber(String name) {
        return new DefaultSubscriber<>(name);
    }

    private static final Faker faker = Faker.instance();

    public static Faker faker() {
        return faker;
    }

    public static void main(String[] args) {
        var mono = Mono.just(1);
        mono.subscribe(getSubscriber());
        mono.subscribe(getSubscriber("subscriber1"));
        mono.subscribe(getSubscriber("subscriber2"));
        mono.subscribe(getSubscriber("subscriber3"));
    }
}
