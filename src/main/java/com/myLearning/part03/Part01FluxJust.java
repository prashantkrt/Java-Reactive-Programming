package com.myLearning.part03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.List;

public class Part01FluxJust {

    private static final Logger logger = LoggerFactory.getLogger(Part01FluxJust.class);

    public static void main(String[] args) {

        // like we have List.of() for java.util.List
        // we have Flux.just() for reactor.core.publisher.Flux

        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(list);
        list.forEach(i -> logger.info("{}", i));

        //similarly with flux
        Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(integerFlux); //FluxArray
        integerFlux.subscribe(i -> logger.info("{}", i));
    }
}
