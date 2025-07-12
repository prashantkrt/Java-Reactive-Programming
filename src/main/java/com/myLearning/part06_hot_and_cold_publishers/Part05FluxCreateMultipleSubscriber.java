package com.myLearning.part06_hot_and_cold_publishers;

import com.myLearning.part06_hot_and_cold_publishers.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;
// This class is used to generate names and push them to the FluxSink
// It implements Consumer<FluxSink<String>> so it can be passed to Flux.create()
class NameGenerator implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    // This is the overridden method from the Consumer interface
    // It is called **once** by Flux.create(...) when the first subscriber subscribes
    @Override
    public void accept(FluxSink<String> sink) {
        this.sink = sink; // Store the sink to use later to emit data
    }

    // Generates a fake name and pushes it to the Flux via sink.next(...)
    public void generate() {
        sink.next(Util.faker().name().firstName());
    }

    // Marks the stream as complete
    public void complete() {
        sink.complete();
    }
}

public class Part05FluxCreateMultipleSubscriber {

    public static void main(String[] args) {
        var nameGenerator = new NameGenerator();

        // Flux.create(...) will call nameGenerator.accept(sink)
        // This creates a cold Flux and internally assigns the `sink` only once (to the first subscriber's stream)
        Flux<String> nameFlux = Flux.create(nameGenerator);

        // First subscriber subscribes
        // At this point, Flux.create will call accept() and provide the FluxSink
        nameFlux.subscribe(Util.getSubscriber("subscriber1"));

        // Second subscriber subscribes
        // But the same Flux (cold publisher) is used — it doesn't create a new sink
        // The second subscriber reuses the same Flux source but is not wired into the single existing sink
        nameFlux.subscribe(Util.getSubscriber("subscriber2"));

        // Now, when you call generate(), you're pushing data via a single sink instance
        // That sink is tied to **only one subscriber's stream** — usually the latest one that causes the sink to be set
        // Therefore, **only subscriber2 receives the data**, because it likely overwrote the previous sink reference
        for (int i = 0; i < 100; i++) {
            nameGenerator.generate(); // Emits data to the current sink (bound to subscriber2 only)
        }

        // Because Flux.create() creates a cold publisher and only one sink is used,
        // the emission is NOT multicast — only the subscriber tied to the sink receives the values.
    }
}

