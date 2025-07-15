package com.myLearning.part08_repeat_retry;

import reactor.core.publisher.Flux;

//repeat() is for successful completion cycles.

//repeat()	Infinite repeats	Use .take() to limit
//repeat(n)	Fixed repeat count	Original + n repeats
//repeat(BooleanSupplier)	Conditional repeat	Controlled by custom logic
//repeatWhen(Function)	Reactive conditional repeat	Powerful, uses signals to control repeats

public class Part01RepeatBasic {
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
