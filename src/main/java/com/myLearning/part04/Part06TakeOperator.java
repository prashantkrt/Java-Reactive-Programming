package com.myLearning.part04;

import com.myLearning.part04.common.Util;
import reactor.core.publisher.Flux;

public class Part06TakeOperator {
    public static void main(String[] args) {
       getData();
    }
    private static void take(){
        Flux.range(1, 10)
                .log("take")
                .take(3)
                .log("sub")
                .subscribe(Util.getSubscriber());
    }

    // 1 2 3 4
    private static void takeWhile() {
        Flux.range(1, 10)
                .log("take")
                .takeWhile(i -> i < 5) // stop when the condition is not met
                .log("sub")
                .subscribe(Util.getSubscriber());
    }

    // 1 2 3 4 5
    private static void takeUntil() {
        Flux.range(1, 10)
                .log("take")
                .takeUntil(i -> i < 5) // stop when the condition is met + allow the last item
                .log("sub")
                .subscribe(Util.getSubscriber());
    }

    //  8 9 10
    private static void takeLast() {
        Flux.range(1, 10)
                .log("take")
                .takeLast(3)
                .log("sub")
                .subscribe(Util.getSubscriber());
    }


    private static void getData() {
        Flux<String> names = Flux.just("John", "Jack", "Jill", "Stop", "Jane", "Joe");

        System.out.println("Using takeWhile:");
        names.takeWhile(name -> !name.equals("Stop"))
                .subscribe(System.out::println);

        System.out.println("\nUsing takeUntil:");
        names.takeUntil(name -> name.equals("Stop"))
                .subscribe(System.out::println);
    }

}
