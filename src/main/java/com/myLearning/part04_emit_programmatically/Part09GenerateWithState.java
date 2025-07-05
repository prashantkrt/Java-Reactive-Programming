package com.myLearning.part04_emit_programmatically;

import com.myLearning.part04_emit_programmatically.common.Util;
import reactor.core.publisher.Flux;
//public static <T> Flux<T> generate(Supplier<T> stateSupplier, BiFunction<T, SynchronousSink<T>, T> generator)
public class Part09GenerateWithState {
    public static void main(String[] args) {
        Flux.generate(
                () -> 0, // Supplier: Initializes the state to 0
                (counter, sink) -> { // Generator: Generates data and manages the state
                    var country = Util.faker().country().name();

                    sink.next(country);

                    counter++;

                    if (counter == 10 || country.equalsIgnoreCase("Canada")) {
                        sink.complete(); // Completes the sequence
                    }

                    return counter;
                })
                .subscribe(Util.getSubscriber()); // Subscribe and consume the values
    }
}