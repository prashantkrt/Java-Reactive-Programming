package com.myLearning.part05_operators;

import com.myLearning.part04_emit_programmatically.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class Part11Transform2 {

    private static final Logger log = LoggerFactory.getLogger(Part11Transform2.class);

    record Customer(int id, String name){}
    record PurchaseOrder(String productName, int price, int quantity){}

    public static void main(String[] args) {

        //transform(...) takes a Function<Flux<T>, Publisher<R>>.
        //Function.identity() just returns the input stream unchanged.
        //isDebug ? addDebugger() : Function.identity() lets you conditionally plug in debugging behavior.
        var isDebug = false;

        getCustomers()
                .transform(isDebug == true ? addDebugger() : Function.identity())
                .subscribe(System.out::println);

        getPurchaseOrders()
                .transform(addDebugger())
                .subscribe();
    }

    //mapping to the customers
    private static Flux<Customer> getCustomers(){
        return Flux.range(1, 3)
                .map(i -> new Customer(i, Util.faker().name().firstName()));
    }

    //mapping to the purchase orders
    private static Flux<PurchaseOrder> getPurchaseOrders(){
        return Flux.range(1, 5)
                .map(i -> new PurchaseOrder(Util.faker().commerce().productName(), i, i * 10));
    }

    // pass flux<T> and return flux<T>
    private static <T> Function<Flux<T>, Flux<T>> addDebugger(){
        return flux -> flux
                .doOnNext(i -> log.info("received: {}", i))
                .doOnComplete(() -> log.info("completed"))
                .doOnError(err -> log.error("error", err));

    }

}
