package com.myLearning.part03;

import com.myLearning.part01.subscriber.SubscriberImpl;
import com.myLearning.part03.client.ExternalServiceClient;
import reactor.core.publisher.Flux;

public class Part12NonBlockingIO {
    public static void main(String[] args) {

        var client = new ExternalServiceClient();
        var subscriber = new SubscriberImpl();

        Flux<String> names = client.getNames();
        names.subscribe(subscriber);
        subscriber.getSubscription().request(3);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
