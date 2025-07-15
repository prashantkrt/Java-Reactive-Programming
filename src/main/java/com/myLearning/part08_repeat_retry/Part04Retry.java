package com.myLearning.part08_repeat_retry;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

//retry() Infinite retry ( dangerous)
//retry(long n)	Retry n times after error
//retryBackoff(n, Duration)	Retry with backoff delay between retries
//retryWhen()	Fully custom retry logic (delay, max attempts, etc.)
public class Part04Retry {

    private static final Logger log = LoggerFactory.getLogger(Part04Retry.class);

    public static void main(String[] args) {

        demo1();
    }
    private static void demo1() {
        getCountryName()
                .retry(2) // jab error aayega
                .subscribe(Util.getSubscriber("Demo 1 Subscriber"));
    }

    private static Mono<String> getCountryName() {
        var atomicInteger = new AtomicInteger(0);
        return Mono.fromSupplier(() -> {
                    if (atomicInteger.incrementAndGet() < 5) {
                        throw new RuntimeException("oops");
                    }
                    return Util.faker().country().name();
                })
                .doOnError(err -> log.info("ERROR: {}", err.getMessage()))
                .doOnSubscribe(s -> log.info("subscribing"));

    }
}
