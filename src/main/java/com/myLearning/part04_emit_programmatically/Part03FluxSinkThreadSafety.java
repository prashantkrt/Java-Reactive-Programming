package com.myLearning.part04_emit_programmatically;

import com.myLearning.part04_emit_programmatically.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.function.Consumer;

class Generator implements Consumer<FluxSink<String>> {

    private FluxSink<String> sink;

    @Override
    public void accept(FluxSink<String> sink) {
        this.sink = sink;
    }

    public void generate() {
        this.sink.next(Util.faker().name().firstName());
    }

    public void complete() {
        this.sink.complete();
    }

}


/*
 * Flux Sink is thread safe
 * */
public class Part03FluxSinkThreadSafety {

    private final static Logger logger = LoggerFactory.getLogger(Part03FluxSinkThreadSafety.class);

    public static void main(String[] args) {

        //demo1();
        demo2();
    }

    // example where thread safety is not guaranteed.
    // ArrayList is not thread safe
    public static void demo1() {
        var list = new ArrayList<Integer>();
        Runnable runnable = () -> {
            for (int i = 0; i < 100; i++) {
                list.add(i);
            }
        };

        // creating 10 threads simultaneously and starting them
        for (int i = 0; i < 10; i++) {
            // Thread thread = new Thread(runnable);
            // thread.start();
            // or we can use
            Thread.ofPlatform().start(runnable);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("the size of the list: {}", list.size()); //the size of the list: 989 but it should be 1000
    }


    //Using Flux Sink, it is thread safe
    private static void demo2() {
        var list = new ArrayList<String>();

        var generator = new Generator();
        var flux = Flux.create(generator);
        flux.subscribe(name -> list.add(name),
                err -> logger.error("Error: {}", err),
                () -> logger.info("Completed"));

        Runnable runnable = () -> {
            for (int i = 0; i < 100; i++) {
                generator.generate();
            }
        };

        for (int i = 0; i < 10; i++) {
            Thread.ofPlatform().start(runnable);
        }

        try {
            Thread.sleep(3000);
            generator.complete();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("the size of the list using flux sink: {}", list.size());
    }

}

