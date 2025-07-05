package com.myLearning.part04_emit_programmatically;

import com.myLearning.part04_emit_programmatically.common.Util;
import reactor.core.publisher.Flux;

public class Part08FluxGenerateUntil {
    public static void main(String[] args) {
        demo2();
    }

    private static void demo1() {
        Flux<String> flux = Flux.generate(synchronousSink -> {
            var countryName = Util.faker().country().name();
            if (countryName.equalsIgnoreCase("canada")) {
                synchronousSink.complete();
            }
            synchronousSink.next(countryName);
        });
        flux.subscribe(Util.getSubscriber());
    }

    private static void demo2() {
        Flux.<String>generate(synchronousSink -> {
                    var country = Util.faker().country().name();
                    synchronousSink.next(country);
                }).takeUntil(c -> c.equalsIgnoreCase("canada"))
                .subscribe(Util.getSubscriber());
    }
}
