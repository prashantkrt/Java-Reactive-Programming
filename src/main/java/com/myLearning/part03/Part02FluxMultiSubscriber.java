package com.myLearning.part03;

import com.myLearning.part03.common.Util;
import reactor.core.publisher.Flux;

public class Part02FluxMultiSubscriber {
    public static void main(String[] args) {

        var flux = Flux.just(1,2,3,4,5,6);
        flux.subscribe(Util.getSubscriber("subscriber1"));
        flux.subscribe(Util.getSubscriber("subscriber2"));

        flux.filter(i->i%2==0).map(i->i*2).subscribe(Util.getSubscriber("subscriber3"));

    }
}
