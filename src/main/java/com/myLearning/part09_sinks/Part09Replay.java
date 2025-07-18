package com.myLearning.part09_sinks;


import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Sinks;


/*
| .all(n) Value   | Behavior                                      |
| --------------- | --------------------------------------------- |
|  .all(1)        |  Replays last 1 item to new subscribers.      |
|  .all(3)        |  Replays last 3 items.                        |
|  .all()         |  Replays all items since start.               |
*/
public class Part09Replay {
    public static void main(String[] args) {
        demo1();
    }

    private static void demo1(){

        // handle through which we would push items
        var sink = Sinks.many().replay().all(1); //It will replay the last 1 value to any late subscriber.

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        flux.subscribe(Util.getSubscriber("sam"));
        flux.subscribe(Util.getSubscriber("mike"));

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        Util.sleepSeconds(2);

        flux.subscribe(Util.getSubscriber("jake"));
        sink.tryEmitNext("new message");

    }

}
