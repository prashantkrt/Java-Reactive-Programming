package com.myLearning.part05_operators;

import com.myLearning.part04_emit_programmatically.common.Util;
import reactor.core.publisher.Mono;

public class Part07OnErrorResume {
    public static void main(String[] args) {
        onErrorResume();
    }

    private static void onErrorResume() {
        Mono.error(new RuntimeException("oops"))
                .onErrorResume(ArithmeticException.class, ex -> fallback1())
                .onErrorResume(ex -> fallback2())
                .onErrorReturn(-5)
                .subscribe(Util.getSubscriber());
    }

    private static Mono<Integer> fallback1() {
        return Mono.fromSupplier(() -> Util.faker().random().nextInt(10, 100));
    }

    private static Mono<Integer> fallback2() {
        return Mono.fromSupplier(() -> Util.faker().random().nextInt(100, 1000));
    }
}
