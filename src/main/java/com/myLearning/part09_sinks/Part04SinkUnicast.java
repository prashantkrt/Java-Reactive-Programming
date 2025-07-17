package com.myLearning.part09_sinks;

import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Sinks;

public class Part04SinkUnicast {
    public static void main(String[] args) {
        demo2();
    }

    private static void demo1(){
        // handle through which we would push items
        // onBackPressureBuffer - unbounded queue
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
    }

    // we can not have multiple subscribers
    private static void demo2(){
        // handle through which we would push items
        // onBackPressureBuffer - unbounded queue
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        flux.subscribe(Util.getSubscriber("Subscriber 1"));
        flux.subscribe(Util.getSubscriber("Subscriber 2")); //error occurred java.lang.IllegalStateException: Sinks.many().unicast() sinks only allow a single Subscriber
    }

}
