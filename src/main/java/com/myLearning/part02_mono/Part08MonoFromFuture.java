package com.myLearning.part02_mono;

import com.myLearning.part02_mono.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Part08MonoFromFuture {

    private static final Logger logger = LoggerFactory.getLogger(Part08MonoFromFuture.class);

    public static void main(String[] args) {
        //var mono = Mono.fromFuture(getName()); // not lazy

        var monoLazy = Mono.fromFuture(()->getName()); // supplier of completable future is not lazy
        monoLazy.subscribe(Util.getSubscriber());

        // Prevent the main thread from exiting early
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // async
    // its not lazy
    private static CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("getting name");
            return Util.getFaker().name().firstName();
        });
    }
}
