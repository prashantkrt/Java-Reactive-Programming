package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/*
    Zip
    - we will subscribe to all the producers at the same time
    - all or nothing
    - all producers will have to emit an item
 */
public class Part10Zip {

    record Car(String body, String engine, String tires){}

    public static void main(String[] args) {
        Flux.zip(getBody(), getEngine(), getTires())
                .map(t -> new Car(t.getT1(), t.getT2(), t.getT3()))
                .subscribe(System.out::println);

        //Car[body=body-1, engine=engine-1, tires=tires-1]
        //Car[body=body-2, engine=engine-2, tires=tires-2]
        //Car[body=body-3, engine=engine-3, tires=tires-3]

        Util.sleepSeconds(5);

    }

    private static Flux<String> getBody(){
        return Flux.range(1, 5)
                .map(i -> "body-" + i)
                .delayElements(Duration.ofMillis(100));
    }

    private static Flux<String> getEngine(){
        return Flux.range(1, 3)
                .map(i -> "engine-" + i)
                .delayElements(Duration.ofMillis(200));
    }

    private static Flux<String> getTires(){
        return Flux.range(1, 10)
                .map(i -> "tires-" + i)
                .delayElements(Duration.ofMillis(150));
    }
}
