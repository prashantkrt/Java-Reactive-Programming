package com.myLearning.part08_repeat_retry;

import reactor.core.publisher.Flux;

//repeat() is for successful completion cycles.
public class Part01Repeat {
    public static void main(String[] args) {
        Flux<String> flux = Flux.just("A", "B", "C")
                .doOnComplete(() -> System.out.println("Completed emitting values"))
                .repeat(2);  // repeats the entire sequence 2 times

        flux.subscribe(System.out::println);
        //A
        //B
        //C
        //Completed emitting values
        //A
        //B
        //C
        //Completed emitting values
        //A
        //B
        //C
        //Completed emitting values
    }
}
