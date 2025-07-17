package com.myLearning.part09_sinks;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;

public class Part02SinkOneEmitFailureHandler {

    public static final Logger logger = LoggerFactory.getLogger(Part02SinkOneEmitFailureHandler.class);

    public static void main(String[] args) {

        var sink = Sinks.one();

        var mono = sink.asMono();
        mono.subscribe(Util.getSubscriber("Subscriber1"));

        //emit failure handler
        /*
         * @FunctionalInterface
         * public interface EmitFailureHandler {
         *     boolean onEmitFailure(SignalType signalType, EmitResult emitResult);
         * }
         */
        sink.emitValue("Hello World", (signalType, emitResult) -> {
            logger.info("Signal type: {}", signalType.name());
            logger.info("Result: {}", emitResult.name());

            if (signalType == SignalType.ON_ERROR) {
                logger.error("Error occurred for the value: {}", emitResult);
            }
            return false;
        });

        // we can not emit after complete
        // Sinks.One can emit only ONE value.
        // After it emits first value, the sink is terminated — you can't emit again.
        // emit failure handler
        sink.emitValue("Hello World 2", (signalType, emitResult) -> {
            logger.info("Emit attempt - Signal Type: {}, Emit Result: {}", signalType, emitResult);  // Emit attempt - Signal Type: onNext, Emit Result: FAIL_TERMINATED

            // Log errors only when emit fails (i.e., not successful)
            if (emitResult.isFailure()) {
                logger.error("Emit failed for SignalType: {}, Reason: {}", signalType, emitResult); // Emit failed for SignalType: onNext, Reason: FAIL_TERMINATED
            }

            // Returning false means: don't retry, just log and skip
            return false;
        });
        //OK	               Success
        //FAIL_TERMINATED	   Sink is already closed (value/error emitted)
        //FAIL_OVERFLOW	       Buffer is full (for unicast/multicast)
        //FAIL_NON_SERIALIZED  Concurrent emissions from multiple threads

    }
}
