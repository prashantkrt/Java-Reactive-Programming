package com.myLearning.part05_operators;

import com.myLearning.part04_emit_programmatically.common.Util;
import reactor.core.publisher.Flux;

// Filter + map
public class Part01Handle {
    public static void main(String[] args) {

        //example 1
        Flux<String> countries = Flux.create(fluxSink -> {
            for (int i = 0; i < 10; i++) {
                fluxSink.next(Util.faker().country().name());
            }
            fluxSink.complete();
        });

        countries.handle((country, sink) -> {
            if (!country.equals("Canada")) {
                sink.next(country.toUpperCase());
            }
        }).subscribe(System.out::println);

        //example 2
        Flux.range(1, 10)
                .handle((i, sink) -> {
                    if (i % 2 == 0) {
                        sink.next(i);
                    }
                }).subscribe(System.out::println);


        // example 3
        Flux.range(1, 10)
                .handle((i, sink) -> {
                    if (i % 2 != 0) {
                        sink.next(i * i);
                    }
                })
                .subscribe(System.out::println);

        //example 4
        Flux<Integer> flux = Flux.range(1, 10)
                .handle((i, sink) -> {
                    switch (i) {
                        case 1 -> sink.next(-1);
                        case 2, 4 -> {
                            //do nothing
                        }
                        case 3 -> sink.next(-3);
                        case 5 -> sink.error(new RuntimeException("Error"));
                        case 6 -> sink.next(-6);
                        default -> sink.next(i);
                    }
                });

        flux.subscribe(System.out::println,
                System.err::println,
                () -> System.out.println("Completed"));

    }
}
