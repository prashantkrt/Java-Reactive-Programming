package com.myLearning.part09_sinks;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Part05SinkThreadSafety {

    public static final Logger logger = LoggerFactory.getLogger(Part05SinkThreadSafety.class);

    public static void main(String[] args) {
        demo2();
    }

    private static void demo1() {
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        var flux = sink.asFlux();

        var list = new ArrayList<>();

        flux.subscribe(data -> list.add(data));

        // Revision
        // runAsync()	Runs task with NO result	CompletableFuture<Void>	You just want to run something in the background, no result expected
        // supplyAsync()	Runs task with result	CompletableFuture<T>	You run something in the background and get result back
        // it’s not compulsory to call get() —
        // The task WILL run in the background because CompletableFuture uses ForkJoinPool.commonPool() by default.

        // intentionally doing this to test
        for (int i = 0; i < 1000; i++) {
            var data = i;
            CompletableFuture.runAsync(() -> {
                sink.tryEmitNext(data);
            });

        }

        Util.sleepSeconds(5);
        logger.info("list size: {}", list.size()); // incorrect list size as 121 instead of 1000
    }


    // fix for the demo1
    // Sink itself is thread-safe → true.
    // BUT — Sinks.many().unicast() requires serialized emission → meaning only one thread at a time should call emitNext().
    // 1000 threads (via runAsync) are emitting at the same time.
    // Internally, tryEmitNext() fails with FAIL_NON_SERIALIZED because it detects multiple threads writing concurrently.
    // It rejects many emits silently (if you don’t handle the return value).
    private static void demo2() {
        var sink = Sinks.many().unicast().onBackpressureBuffer();
        var flux = sink.asFlux();

        // arraylist is not thread safe.
        // intentionally chosen for demo purposes.
        var list = new ArrayList<>();
        flux.subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            var j = i;
            CompletableFuture.runAsync(() -> {
                sink.emitNext(j, (signal, emitResult) -> {
                    logger.info("Emit attempt - Signal Type: {}, Emit Result: {}", signal, emitResult);
//                    if (emitResult.isFailure()) {
//                        logger.error("Error for the value with signal type : {}: {}", emitResult, signal);
//                    }
                    return Sinks.EmitResult.FAIL_NON_SERIALIZED.equals(emitResult); // will keep retrying and this will give the result as per required like 1000 as list size
                   // return false;  this will not retry and will drop the current failed result
                });
            });
        }

        Util.sleepSeconds(5);
        logger.info("list size is : {}", list.size()); // list size is : 1000
    }

}

//Sinks.EmitResult possible values

//OK
//Emission succeeded.
//No need to retry.

//FAIL_ZERO_SUBSCRIBER
//There are no subscribers listening to the sink.
//Value is dropped (unless you have a replay sink that caches).

//FAIL_OVERFLOW
//Sink has backpressure / buffer full.
//Could happen with multicast().onBackpressureBuffer(n) if buffer is full.
//Usually indicates you can retry later.

//FAIL_TERMINATED
//Sink has been completed or errored.
//Value cannot be emitted, will be dropped.

//FAIL_CANCELLED
//Sink has been cancelled by subscriber.
//Value will be dropped.

//FAIL_NON_SERIALIZED
//Concurrent emission into a sink that only supports serialized emissions.
//Returning true in failure handler → retry emission.


/*
        | EmitResult             | Meaning                          |
        | ---------------------- | -------------------------------- |
        |  OK                    | Success                          |
        |  FAIL_ZERO_SUBSCRIBER  | No subscriber                    |
        |  FAIL_OVERFLOW         | Buffer full / backpressure       |
        |  FAIL_TERMINATED       | Sink completed / errored         |
        |  FAIL_CANCELLED        | Sink cancelled                   |
        |  FAIL_NON_SERIALIZED   | Concurrent emission, needs retry |
*/
