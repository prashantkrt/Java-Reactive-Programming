package com.myLearning.part03_flux.common;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

public class AbstractHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    protected final HttpClient client;

    public AbstractHttpClient() {
        // LoopResources is like an I/O thread pool for Netty
        // 1 thread → All I/O (even across many connections) goes through one thread.
        // 2 threads → Load is split between two event loops.
        // 4 threads → More parallelism for I/O-heavy workloads.
        var loopResources = LoopResources.create("reactor", 2, true);
        this.client = HttpClient.create().runOn(loopResources).baseUrl(BASE_URL);
    }
}
