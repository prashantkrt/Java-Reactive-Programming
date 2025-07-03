package com.myLearning.ToBeRemembered;

import java.util.function.Consumer;

// Simulate Subscriber (like what .subscribe(...) does)
interface MySubscriber<T> {
    void onNext(T t);
    void onComplete();
    void onError(Throwable e);
}

// Simulate FluxSink
interface MyFluxSink<T> {
    void next(T t);
    void complete();
    void error(Throwable e);
}

// Simulate Flux
class MyFlux<T> {
    private final Consumer<MyFluxSink<T>> emitter;

    public MyFlux(Consumer<MyFluxSink<T>> emitter) {
        this.emitter = emitter;
    }

    public void subscribe(MySubscriber<T> subscriber) {
        // Create a sink that knows how to send data to subscriber
        MyFluxSink<T> sink = new MyFluxSink<>() {
            @Override
            public void next(T t) {
                subscriber.onNext(t);  // send to subscriber
            }

            @Override
            public void complete() {
                subscriber.onComplete();
            }

            @Override
            public void error(Throwable e) {
                subscriber.onError(e);
            }
        };

        // This is the key part:
        // Call the consumer with the sink => this is where you emit data
        emitter.accept(sink);
    }
}


public class CustomFluxSink {
    public static void main(String[] args) {
        // Simulating Flux.create(...)
        MyFlux<String> myFlux = new MyFlux<>(new Consumer<MyFluxSink<String>>() {
            @Override
            public void accept(MyFluxSink<String> sink) {
                sink.next("A");
                sink.next("B");
                sink.next("C");
                sink.complete();
            }
        });

        // Simulating .subscribe(...)
        myFlux.subscribe(new MySubscriber<>() {
            @Override
            public void onNext(String s) {
                System.out.println("Received: " + s);
            }

            @Override
            public void onComplete() {
                System.out.println("Stream completed.");
            }

            @Override
            public void onError(Throwable e) {
                System.err.println("Error: " + e.getMessage());
            }
        });
    }
}
