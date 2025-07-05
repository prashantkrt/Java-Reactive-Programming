package com.myLearning.part03_flux;

import com.myLearning.part03_flux.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

//public static <T> Flux<T> defer(Supplier<? extends Publisher<? extends T>> supplier)
public class Part10FluxDefer {
    public static void main(String[] args) {

        Flux<String> flux = Flux.defer(() -> {
            System.out.println("Generating fresh list...");
            return getNames(10);
        });

        //now it will be called and created only when subscribed
       // flux.subscribe(Util.getSubscriber());


        List<Integer> list = List.of(1, 2, 3,4,5,6,7,8,9,10);
        Flux<Integer> integerFlux = Flux.defer(() -> Flux.fromStream(()->list.stream()));
        integerFlux.subscribe(Util.getSubscriber("My Custom Flux"));

    }

    private static Flux<String> getNames(int count) {
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
