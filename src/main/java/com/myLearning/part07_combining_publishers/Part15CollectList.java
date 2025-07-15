package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/*
    To collect the items received via Flux. Assuming we will have finite items!
 */
public class Part15CollectList {
    public static void main(String[] args) {
        Mono<List<Integer>> listMono = Flux.range(1, 10)
                .collectList();
        listMono.subscribe(Util.getSubscriber()); //Received [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    }
}
