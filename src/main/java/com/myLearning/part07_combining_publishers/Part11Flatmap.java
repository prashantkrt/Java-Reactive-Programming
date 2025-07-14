package com.myLearning.part07_combining_publishers;

import com.myLearning.part07_combining_publishers.applications.Order;
import com.myLearning.part07_combining_publishers.applications.OrderService;
import com.myLearning.part07_combining_publishers.applications.User;
import com.myLearning.part07_combining_publishers.applications.UserService;
import com.myLearning.part07_combining_publishers.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Part11Flatmap {

    public static void main(String[] args) {

         /*
           => Example 1
           To get all the orders for all the users
           We have three services
            1. user service
            2. order service
            3. payment service
         */

        Flux<User> users = UserService.getAllUsers();
        System.out.println(users);

        // each user can have multiple orders
        Flux<Order> orderFlux = users.flatMap(user -> OrderService.getUserOrders(user.id()));// Flux<Order>

        orderFlux.subscribe(i->System.out.println("Example 1: "+i));

        /*
           => Example 2
           We have a username, and we have to get the orders for that user
           we have three services
            1. user service
            2. order service
            3. payment service
         */

        String username = "John";
        Mono<Integer> userFlux = UserService.getUserId(username);

        // When you have Mono.flatMap(), it expects Mono → Mono.
        // But we have Mono → Flux so you should use flatMapMany().

        // flatMapMany because Mono → Flux
        Flux<Order> order = userFlux.flatMapMany(userId -> OrderService.getUserOrders(userId));
        order.subscribe(i->System.out.println("Example 2: "+i));

        //  or else we have to do this way if not have used flatMapMany()
        //  collect orders into a Mono<List<Order>>
        // Mono<List<Order>> ordersMono = userMono.flatMap(userId ->
        //        OrderService.getUserOrders(userId).collectList()
        // );


        Util.sleepSeconds(10);
    }
}
