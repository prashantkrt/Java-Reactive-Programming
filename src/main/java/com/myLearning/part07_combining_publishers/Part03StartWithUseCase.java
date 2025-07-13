package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

class NameGenerator {

    private static final Logger log = LoggerFactory.getLogger(NameGenerator.class);
    private final List<String> redis = new ArrayList<>(); // intentional for demo

    public Flux<String> generateNames() {
        return Flux.generate(sink -> {
                    log.info("generating name");
                    Util.sleepSeconds(1);
                    var name = Util.faker().name().firstName();
                    redis.add(name); // intentional for demo
                    sink.next(name);
                })
                .startWith(redis)
                .cast(String.class);
    }
}

public class Part03StartWithUseCase {

    public static void main(String[] args) {

        var nameGenerator = new NameGenerator();

        nameGenerator.generateNames()
                .take(2)
                .subscribe(Util.getSubscriber("John")); // redis = [Leandro, Diamond]

        Util.sleepSeconds(5);

        nameGenerator.generateNames()
                .take(2)   // redis = [Leandro, Diamond]
                .subscribe(Util.getSubscriber("Peter"));

//        Util.sleepSeconds(5);
//
//        nameGenerator.generateNames()
//                .take(3)
//                .subscribe(Util.getSubscriber("Smith"));

        Util.sleepSeconds(10);

    }
}

//  First time it's called:
//- It starts generating names.
//- It logs generating name twice (as take(2) allows only two emissions).
//- It stores both names in the redis list (Leandro, Diamond).
//- It completes.
// redis = [Leandro, Diamond]

// Second time it's called:
// .startWith(redis)    redis values emitted *before* generating anything new
// Since redis already contains two names (Leandro, Diamond), and you used .take(2),
// the Flux completes immediately after emitting these two names — it never even reaches the Flux.generate(...) part.

