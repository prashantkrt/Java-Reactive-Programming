package com.myLearning.part04;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

/*
create a flux and emit the items programmatically
public static <T> Flux<T> create(Consumer<FluxSink<T>> emitter)
 */
public class Part01FluxCreate {
    public static void main(String[] args) {

        // on Flux.just() data is already ready and nothing dynamic is happening
        Flux<String> lunchBox = Flux.just("Rice", "Dal", "Sabzi");
        lunchBox.subscribe(System.out::println);

        Flux<String> dynamicFlux = Flux.create(sink -> {
            for (int i = 1; i <= 3; i++) {
                sink.next("Event " + i);
            }
            sink.complete(); // Always complete
        });
        dynamicFlux.subscribe(System.out::println);


        Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> sink) {
                sink.next("Event 1");
                sink.next("Event 2");
                sink.next("Event 3");
                sink.complete();
            }
        });


    }
}
/*
public interface FluxSink<T> {

    // Emit a single item
    FluxSink<T> next(T t);

    // Signal that the stream is completed
    void complete();

    // Signal an error
    void error(Throwable e);

    // Optional: Request how many items the subscriber wants
    FluxSink<T> onRequest(LongConsumer consumer);

    // Optional: Set a cancellation callback
    FluxSink<T> onCancel(Disposable d);

    // Optional: Set a callback for termination (completion or error)
    FluxSink<T> onDispose(Disposable d);

    // Get the current requested demand
    long requestedFromDownstream();

    // Optional: Get the current FluxSink overflow strategy
    OverflowStrategy getOverflowStrategy();
}
 */
