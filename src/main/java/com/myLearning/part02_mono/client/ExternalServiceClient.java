package com.myLearning.part02_mono.client;

import com.myLearning.part02_mono.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {

    public Mono<String> getProductName(int id) {
        return this.client.get() // Starts a non-blocking GET request
                .uri("/demo01/product/" + id) // Sets the request path
                .responseContent() // Extracts the raw response body => ByteBufFlux  Step 1: Flux<ByteBuf>
                .asString() //Used with .responseContent() to Converts body to Flux<String> => Step 2: Flux<String>
                .next(); //Takes the first item and wraps in Mono =>   Step 3: Mono<String>
    }
}


// Flux<String> names = Flux.just("Prashant", "Kumar", "Tiwary");
// Mono<String> firstName = names.next();
// firstName.subscribe(System.out::println); // Output: Prashant