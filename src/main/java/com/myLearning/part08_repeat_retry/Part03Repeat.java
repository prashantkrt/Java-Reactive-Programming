package com.myLearning.part08_repeat_retry;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/*
    repeat operator simply resubscribes when it sees complete signal.
    it does not like error signal.
 */
//repeat()	Infinite repeats	Use .take() to limit
//repeat(n)	Fixed repeat count	Original + n repeats
//repeat(BooleanSupplier)	Conditional repeat	Controlled by custom logic
//repeatWhen(Function)	Reactive conditional repeat	Powerful, uses signals to control repeats

public class Part03Repeat {

    private static final Logger log = LoggerFactory.getLogger(Part03Repeat.class);
    public static void main(String[] args) {
        demo4();
        Util.sleepSeconds(10);
    }

    private static void demo1() {
        getCountryName()
                .repeat(3)
                .subscribe(Util.getSubscriber("Demo 1 Subscriber"));
    }

    private static void demo2() {
        getCountryName()
                .repeat()
                .takeUntil(c -> c.equalsIgnoreCase("canada"))
                .subscribe(Util.getSubscriber("Demo 2 Subscriber"));
    }

    private static void demo3() {
        var atomicInteger = new AtomicInteger(0);
        getCountryName()
                .repeat(() -> {
                    log.info("Repeat count {}", atomicInteger.get());
                    return atomicInteger.incrementAndGet() < 3;
                })
                .subscribe(Util.getSubscriber("Demo 3 Subscriber"));
    }


    // Normally, repeat() repeats immediately after completion.
    // repeatWhen() allows you to control when and how many times to repeat by using a signal (a Flux).
    private static void demo4() {
        getCountryName()
                .repeatWhen(flux -> flux.delayElements(Duration.ofSeconds(2)).take(2)) // repeat twice, with delay
                .subscribe(Util.getSubscriber("Demo 4 Subscriber"));
    }

    private static Mono<String> getCountryName() {
        return Mono.fromSupplier(() -> Util.faker().country().name());
    }
}
