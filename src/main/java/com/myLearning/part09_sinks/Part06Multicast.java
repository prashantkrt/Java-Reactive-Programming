package com.myLearning.part09_sinks;

import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Sinks;

public class Part06Multicast {
    public static void main(String[] args) {
        demo2();
    }

    private static void demo1(){

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
        flux.subscribe(Util.getSubscriber("Subscriber 2"));

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        Util.sleepSeconds(2);

        flux.subscribe(Util.getSubscriber("Subscriber 3"));
        sink.tryEmitNext("new message");

    }

    // warmup
    private static void demo2(){

        // handle through which we would push items
        // onBackPressureBuffer - bounded queue
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        Util.sleepSeconds(2);

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
        flux.subscribe(Util.getSubscriber("Subscriber 2"));
        flux.subscribe(Util.getSubscriber("Subscriber 3"));

        sink.tryEmitNext("new message");

    }

}
