package com.myLearning.part04_emit_programmatically;

import com.myLearning.part04_emit_programmatically.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

class NameGenerator implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    @Override
    public void accept(FluxSink<String> sink) {
        this.sink = sink;
    }

    public void generate() {
        this.sink.next(Util.faker().name().firstName());
    }

}


public class Part02FluxCreateRefactor {
    public static void main(String[] args) {

        var nameGenerator = new NameGenerator();
        var nameFlux = Flux.create(nameGenerator);

        nameFlux.subscribe(Util.getSubscriber());

        for (int i = 0; i < 100; i++) {
            nameGenerator.generate();
        }
    }
}
