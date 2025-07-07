package com.myLearning.part05_operators;

import reactor.core.publisher.Flux;

/*
    Similar to error handling.
    Handling empty!
 */
public class Part09SwitchIfEmpty {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .filter(i -> i > 10)
                .switchIfEmpty(fallback())
                .subscribe(i -> System.out.println(i));
    }
    private static Flux<Integer> fallback(){
        return Flux.range(100, 3); //100 200 300
    }
}
