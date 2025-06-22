package com.myLearning.part03;

import com.myLearning.part03.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Part05FluxFromStream {
    public static void main(String[] args) {
        Stream<String> nameStream = Stream.of("Prashant", "Kumar", "Tiwary");

        Flux<String> fluxFromStream = Flux.fromStream(nameStream);

        fluxFromStream.subscribe(
                name -> System.out.println("FromStream: " + name),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed fromStream")
        );

        // Like in the stream, if we try to reuse the stream, it will throw an exception since we can use it only once.
        // We can use the streamSupplier instead to avoid the exception.

        // trying to reuse fluxFromStream stream again, this will throw an exception
        // exception java.lang.IllegalStateException: The Stream has already been operated upon or closed.
        fluxFromStream.subscribe(
                name -> System.out.println("FromStream: " + name),
                err -> System.err.println("Error: " + err),
                () -> System.out.println("Completed fromStream")
        );

        // To avoid the stream being closed, we can pass the supplier of the stream
        var list = List.of(1,2,3,4);

        //supplier of stream
        var flux = Flux.fromStream(() -> list.stream());

        flux.subscribe(Util.getSubscriber("sub1"));
        flux.subscribe(Util.getSubscriber("sub2"));


        List<Integer> list2 = List.of(100, 200, 300);

        // Anonymous class implementation of Supplier<Stream<? extends Integer>>
        Supplier<Stream<? extends Integer>> streamSupplier = new Supplier<Stream<? extends Integer>>() {
            @Override
            public Stream<? extends Integer> get() {
                return list2.stream();
            }
        };

        Flux<Integer> flux2 = Flux.fromStream(streamSupplier);

        flux2.subscribe(
                value -> System.out.println("Received: " + value),
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed"),
                subscription -> subscription.request(2)
        );

    }
}
