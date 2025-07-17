package com.myLearning.part09_sinks;

// Single API response and can have N number of subscribers => Sinks.one() [ Emits only one value or error, multiple subscribers can subscribe but will all get the same single value ]
// Single trigger (no value) => Sinks.empty() [ Emits no value, only completes or errors; useful when you just want to signal completion ]
// Live stream to 1 subscriber => Sinks.many().unicast() [Subscribers can join late and messages will be stared in the memory]
// Live stream to many subscribers => Sinks.many().multicast() [ Multiple subscribers allowed, but late subscribers will NOT receive previous messages, they get data from the point they subscribe ]
// Chat messages or replaying old data and have N number of subscribers => Sinks.many().replay() [ Multiple subscribers allowed; late subscribers receive previously emitted messages (replay) — behavior depends on .all(), .latest(), or .limit(n) ]
/*
        | Sink Type              | Subscribers | Values | Replay | Use Case               |
        | ---------------------- | ----------- | ------ | ------ | ---------------------- |
        |  Sinks.One             | Many        | 1      |   Yes  | API Response           | // Even if subscribers subscribe after value is emitted, they will still get the value (replay behavior happens).
        |  Sinks.Empty           | Many        | 0      |   N/A  | Completion signal      |
        |  Sinks.Many.unicast    | One         | ∞      |   No   | Private data stream    | // Replay to Late Subscriber? => NO since in Unicast = Only 1 subscriber allowed.If subscriber subscribes AFTER emission, it gets buffered data, only if subscribed before completion.After completion → new subscribers are NOT allowed → Late joining is not possible after terminal state.
        |  Sinks.Many.multicast  | Many        | ∞      |   No   | Live data to many      | // No replay
        |  Sinks.Many.replay     | Many        | ∞      |   Yes  | Replaying chat/history | // Yes replay



| Sink Type                  | Description                                 | Bracket Explanation                                                              |
| -------------------------- | ------------------------------------------- | -------------------------------------------------------------------------------- |
| Sinks.one()                | Single value to many                        | [ 1 value or error, multiple subscribers get same value ]                        |
| Sinks.empty()              | Complete without data                       | [ No data, just complete or error ]                                              |
| Sinks.many().unicast()     | Multi-values, 1 subscriber                  | [ Only one subscriber, late joiner gets all buffered messages ]                  |
| Sinks.many().multicast()   | Multi-values, many subscribers              | [ Many subscribers, no replay, subscribers get data from subscribe point ]       |
| Sinks.many().replay()      | Multi-values, many subscribers, with replay | [ Many subscribers, with replay of previous messages to late joiners ]           |

*/

import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public class Part03SinkTypes {
    public static void main(String[] args) {

        demo3();
    }

    //sink.one()
    public static void demo1() {
        var sink = Sinks.one();

        Mono<Object> mono = sink.asMono();
        mono.subscribe(data -> System.out.println("Subscriber 1: " + data));

        sink.tryEmitValue("Hello One!");

        //Subscriber 1: Hello One!
    }

    //sink.empty()
    //Sinks.Empty — Complete without Data
    public static void demo2() {
        var sink = Sinks.empty();

        sink.asMono().subscribe(
                data -> System.out.println("Data: " + data),
                err -> {},
                () -> System.out.println("Completed!")
        );

        sink.tryEmitEmpty();

        //Completed!
    }


    // sink.many().unicast()
    // Sinks.Many.unicast — Single Subscriber, Multiple Values
    public static void demo3() {

        // Backpressure	Too much data from producer, consumer can't keep up
        // onBackpressureBuffer()	Safely stores emitted data until subscriber consumes it
        // It's needed for unicast since unicast allows late subscriber and needs to hold data safely
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        var flux = sink.asFlux();

        flux.subscribe(data -> System.out.println("Unicast Subscriber: " + data));

        sink.tryEmitNext("Unicast 1");
        sink.tryEmitNext("Unicast 2");
        sink.tryEmitNext("30");
        sink.tryEmitComplete();

        //Unicast Subscriber: Unicast 1
        //Unicast Subscriber: Unicast 2
        //Unicast Subscriber: 30

    }

    //Sinks.Many.multicast — Multiple Subscribers, No Replay
    public static void demo4() {
        var sink = Sinks.many().multicast().onBackpressureBuffer();

        var flux = sink.asFlux();

        flux.subscribe(data -> System.out.println("Subscriber 1: " + data));

        sink.tryEmitNext("Message 1");

        flux.subscribe(data -> System.out.println("Subscriber 2: " + data));

        sink.tryEmitNext("Message 2");
        sink.tryEmitComplete();

        //Subscriber 1: Message 1
        //Subscriber 1: Message 2
        //Subscriber 2: Message 2

    }


    //Sinks.Many.replay — Multiple Subscribers with Replay
    public static void demo5() {
        var sink = Sinks.many().replay().all();

        var flux = sink.asFlux();

        sink.tryEmitNext("Replay 1");

        flux.subscribe(data -> System.out.println("Subscriber 1: " + data));

        sink.tryEmitNext("Replay 2");

        flux.subscribe(data -> System.out.println("Subscriber 2: " + data));

        sink.tryEmitNext("Replay 3");
        sink.tryEmitComplete();

        //Subscriber 1: Replay 1
        //Subscriber 1: Replay 2
        //Subscriber 1: Replay 3
        //Subscriber 2: Replay 1
        //Subscriber 2: Replay 2
        //Subscriber 2: Replay 3

    }
}
