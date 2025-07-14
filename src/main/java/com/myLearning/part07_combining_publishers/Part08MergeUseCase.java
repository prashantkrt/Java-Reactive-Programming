package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.UnaryOperator;

import static com.myLearning.part07_combining_publishers.Utilities.fluxLogger;


class Utilities {

    private static final Logger log = LoggerFactory.getLogger(Part08MergeUseCase.class);

    public static <T> UnaryOperator<Flux<T>> fluxLogger(String name) {
        return flux -> flux
                .doOnSubscribe(s -> log.info("subscribing to {}", name))
                .doOnCancel(() -> log.info("cancelling {}", name))
                .doOnComplete(() -> log.info("{} completed", name));
    }
}


record Flight(String airline, Integer price) {
}

class Indigo {

    public static final String AIRLINE = "Indigo";

    public static Flux<Flight> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(2, 10))
                .delayElements(Duration.ofMillis(Util.faker().random().nextInt(100, 500)))
                .map(i -> new Flight(AIRLINE, Util.faker().random().nextInt(100, 1000)))
                .transform(fluxLogger(AIRLINE));
    }
}

class Kingfisher {

    public static final String AIRLINE = "Kingfisher";

    public static Flux<Flight> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(2, 10))
                .delayElements(Duration.ofMillis(Util.faker().random().nextInt(100, 500)))
                .map(i -> new Flight(AIRLINE, Util.faker().random().nextInt(100, 1000)))
                .transform(fluxLogger(AIRLINE));
    }
}

class GOAirlines {

    private static final String AIRLINE = "GOAirlines";

    public static Flux<Flight> getFlights() {
        return Flux.range(1, Util.faker().random().nextInt(3, 5))
                .delayElements(Duration.ofMillis(Util.faker().random().nextInt(300, 800)))
                .map(i -> new Flight(AIRLINE, Util.faker().random().nextInt(400, 900)))
                .transform(fluxLogger(AIRLINE));
    }

}


public class Part08MergeUseCase {

    public static void main(String[] args) {

        Flux<Flight> indigoFlights = Indigo.getFlights();
        Flux<Flight> kingfisherFlights = Kingfisher.getFlights();
        Flux<Flight> goAirlinesFlights = GOAirlines.getFlights();

        Flux<Flight> mergedFlights = Flux.merge(indigoFlights, kingfisherFlights, goAirlinesFlights);

        mergedFlights.subscribe(System.out::println);

        Util.sleepSeconds(5);
    }
}
