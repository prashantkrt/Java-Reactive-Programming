package com.myLearning.part03_flux;

import com.myLearning.part01_intro.subscriber.SubscriberImpl;
import com.myLearning.part03_flux.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

public class Part08FluxVsList {
    public static void main(String[] args) {

        // === BLOCKING ===
        // `getNamesList(10)` generates all 10 names first (with 1-second delay each)
        // The main thread will be blocked for ~10 seconds
        // Only after all names are ready, it will print the result
        // will be blocked to 10 seconds to generate the list
        // it will not print anything until the list is generated
        // it will get blocked for 10 seconds

//        var list = getNamesList(10);
//        System.out.println(list);

        // === NON-BLOCKING / REACTIVE ===
        // getNamesFlux(10) returns a Flux that emits names one by one
        // The delay still happens, but data is pushed to the subscriber immediately when ready
        // This allows streaming behavior and doesn't block the main thread until everything is ready
        // it keeps on pushing the data to subscriber
        var flux = getNamesFlux(10);
        flux.subscribe(Util.getSubscriber("sub1"));


        var flux2 = getNamesFlux(10);
        var subscriber = new SubscriberImpl();
        flux2.subscribe(subscriber);

        subscriber.getSubscription().request(3);
        subscriber.getSubscription().cancel(); // we can cancel the subscription at any time

    }

    private static List<String> getNamesList(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> generateRandomName())
                .toList();
    }

    private static Flux<String> getNamesFlux(int count) {
        return Flux.range(1, count)
                .map(i -> generateRandomName());
    }

    private static String generateRandomName() {
        try {
            Thread.sleep(1000);
        } catch (Exception ignored) {
        }
        return Util.getFaker().name().firstName();
    }
}
