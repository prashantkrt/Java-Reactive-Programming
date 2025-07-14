package com.myLearning.part07_combining_publishers.applications;

import reactor.core.publisher.Mono;

import java.util.Map;
// Payment Service
// Endpoint:
public class PaymentService {

    private static final Map<Integer, Integer> userBalanceTable = Map.of(
            1, 100,
            2, 200,
            3, 300
    );


    public static Mono<Integer> getUserBalance(Integer userId) {
        return Mono.fromSupplier(() -> userBalanceTable.get(userId));
    }

}
