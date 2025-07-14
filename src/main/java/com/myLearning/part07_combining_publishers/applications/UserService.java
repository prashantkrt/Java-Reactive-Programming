package com.myLearning.part07_combining_publishers.applications;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
//User Service
// Endpoint: username -> userId
// Endpoint: all users
public class UserService {

    private static final Map<String, Integer> userTable = Map.of(
            "John", 1,
            "Jane", 2,
            "Kevin", 3
    );

    public static Flux<User> getAllUsers() {
        return Flux.fromIterable(userTable.entrySet())//Map.Entry<String, Integer> with key and value as [John=1, Jane=2, Kevin=3]
                .map(entry -> new User(entry.getValue(), entry.getKey()));
    }

    public static Mono<Integer> getUserId(String username){
        return Mono.fromSupplier(() -> userTable.get(username));
    }
}
