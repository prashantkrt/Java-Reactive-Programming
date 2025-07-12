package com.myLearning.part06_hot_and_cold_publishers;

import com.myLearning.part06_hot_and_cold_publishers.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

class NameGenerator2 implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    @Override
    public void accept(FluxSink<String> sink) {
        this.sink = sink;
    }

    public void generate() {
        sink.next(Util.faker().name().firstName());
    }

    public void complete() {
        sink.complete();
    }
}

public class Part06FluxCreateMultipleSubscriberRefactor {

    public static void main(String[] args) {
        var nameGenerator = new NameGenerator2();

        //now both the subscribers will receive the data from the same source which is shared and Hot publisher
        Flux<String> nameFlux = Flux.create(nameGenerator).share();

        nameFlux.subscribe(Util.getSubscriber("subscriber1"));

        nameFlux.subscribe(Util.getSubscriber("subscriber2"));

        for (int i = 0; i < 100; i++) {
            nameGenerator.generate();
        }
    }
}

