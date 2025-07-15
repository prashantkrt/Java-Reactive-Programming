package com.myLearning.part08_repeat_retry;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

//retry() Infinite retry ( dangerous)
//retry(long n)	Retry n times after error
//retryWhen()	Fully custom retry logic (delay, max attempts, etc.)
// =>  retryWhen(Function<Flux<Throwable>, Publisher<?>>
// =>   retryWhen(Retry retrySpec)
//RetrySpec:
//Retry.fixedDelay(n, Duration)	=> Retry n times, fixed delay
//Retry.backoff(n, Duration) =>	Retry n times with exponential backoff
//Retry.indefinitely() =>	Retry forever
//Retry.max(n) =>	Retry n times (no delay)

public class Part04Retry {

    private static final Logger log = LoggerFactory.getLogger(Part04Retry.class);

    public static void main(String[] args) {
        demo2();
        Util.sleepSeconds(10);
    }

    // retry 2 times after failure
    private static void demo1() {
        getCountryName()
                .retry(2) // jab error aayega
                .subscribe(Util.getSubscriber("Demo 1 Subscriber"));
    }

    //Retry with Backoff Delay
    private static void demo2() {
        getCountryName()
                .retryWhen(Retry.backoff(4, Duration.ofMillis(500)))
                .subscribe(Util.getSubscriber("Demo 2 Subscriber"));
    }


    //  //Retry with Fixed Delay
    private static void demo3() {
        getCountryName()
                .retryWhen(
                        //Retry a maximum of 2 times, with a fixed delay of 1 second between retries
                        Retry.fixedDelay(2, Duration.ofSeconds(1))
                                //Filter condition: only retry if the error is exactly RuntimeException
                                .filter(ex -> RuntimeException.class.equals(ex.getClass()))
                                // What to do when retries are exhausted (after 2 retries):
                                // rethrow the **original error (signal.failure())** to downstream subscriber
                                // After retries are exhausted, re-throw the original exception downstream, so .subscribe() gets the final error.
                                .onRetryExhaustedThrow((spec, signal) -> {
                                    System.out.println("Retries exhausted. Max retries were: " + spec.maxAttempts);
                                    return signal.failure();
                                })
                )
                .subscribe(Util.getSubscriber("Demo 3 Subscriber"));
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
