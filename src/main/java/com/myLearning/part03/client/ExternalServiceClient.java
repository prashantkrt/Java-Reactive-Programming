package com.myLearning.part03.client;

import com.myLearning.part03.common.AbstractHttpClient;
import reactor.core.publisher.Flux;

public class ExternalServiceClient extends AbstractHttpClient {

    public Flux<String> getNames() {
        return this.client.get()
                .uri("/demo02/name/stream")
                .responseContent()
                .asString();
    }

    public Flux<Integer> getPriceChanges() {
        return this.client.get()
                .uri("/demo02/stock/stream")
                .responseContent()
                .asString()
                .map(i -> Integer.parseInt(i));
    }

}
