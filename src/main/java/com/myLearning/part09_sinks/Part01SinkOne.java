package com.myLearning.part09_sinks;

/*
## In Project Reactor, Sinks is a special utility used when you want to programmatically push data into a Flux or Mono — like a bridge between imperative code and reactive streams.

## Normally, you use Flux.range(), Flux.fromIterable(), or Mono.just() — they produce data automatically.
 => But sometimes you want to produce data yourself, on your own logic — when you receive events, inputs, or API calls.
 => That’s when you use a Sink — it allows you to push data manually into a Flux or Mono.

Flux.just(), Flux.range(), Flux.generate() → these are cold publishers.
This means =>
You already have the data (like a static list or random numbers).
You pull the data when someone subscribes.

Now think this way :
But what if,
- we don’t have the data right now?
- we will receive data later — like from WebSocket, Kafka, user input, or some external trigger?
- we want to push data whenever we want?
## So the answer is: we need something to manually push data into Flux/Mono — this is where Sinks come in.

=> Flux.range(1, 5) = automated vending machine that gives you 5 chocolates.
=> Sink = you have a box, and whenever you want, you can put chocolates inside it, and whenever someone comes, they can take chocolates from the box.

| Thing           | Real Life Example                                                      |
| --------------- | ---------------------------------------------------------------------- |
|   Flux.just()   | You prepared tea and served.                                           |
|   Sink          | You have an  empty kettle , you pour tea whenever** guests arrive!     |

| You want to…            | Use this                        |
| ----------------------- | ------------------------------- |
| Already have data → use | Flux.just(), Flux.range(), etc. |
| Data comes later → use  | Sinks                           |

=> Useful when data comes from outside sources, like:
=> External systems (sockets, messaging)
=> User actions (click events)
=> Background tasks
=> External events	Like WebSocket, RabbitMQ, or Kafka, where data comes from external systems — you push it into sink and your reactive pipeline listens.
=>  Manual trigger	Suppose you have some event in your app (button click, API request) — you can push data into sink.

*
*
* */


import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

//Use When: You have to emit a single result, like a response from an API call.
public class Part01SinkOne {
    public static void main(String[] args) {

        Sinks.One<String> sink = Sinks.one();

        Mono<String> mono = sink.asMono();
        mono.subscribe(data -> System.out.println("Received: " + data));

        //will emit now
        sink.tryEmitValue("Hello World");

        sink.tryEmitEmpty(); // empty
        sink.tryEmitError(new RuntimeException("Error")); //will emit error
    }
}
