package com.myLearning.part03_flux.client;

import com.myLearning.part03_flux.common.AbstractHttpClient;
import reactor.core.publisher.Flux;

public class ExternalServiceClient extends AbstractHttpClient {

    public Flux<String> getNames() {
        return this.client.get()
                .uri("/demo02/name/stream")
                .responseContent() //ByteBufFlux
                .asString(); //Flux<String>
    }

    public Flux<Integer> getPriceChanges() {
        return this.client.get()
                .uri("/demo02/stock/stream")
                .responseContent()//ByteBufFlux
                .asString()
                .map(i -> Integer.parseInt(i));
    }

}
