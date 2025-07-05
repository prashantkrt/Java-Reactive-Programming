package com.myLearning.part03_flux;

import com.myLearning.part01_intro.subscriber.SubscriberImpl;
import com.myLearning.part03_flux.client.ExternalServiceClient;
import com.myLearning.part03_flux.subscriber_stock.StockPriceObserver;
import reactor.core.publisher.Flux;

public class Part12NonBlockingIO {
    public static void main(String[] args) {

        var client = new ExternalServiceClient();
        var subscriber = new SubscriberImpl();

        Flux<String> names = client.getNames();
        names.subscribe(subscriber);
        subscriber.getSubscription().request(3);


       var subscriber1 = new StockPriceObserver();
        client.getPriceChanges()
                .subscribe(subscriber1);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
