package com.myLearning.part02_mono;

import com.myLearning.part02_mono.client.ExternalServiceClient;
import com.myLearning.part03_flux.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Part10NonBlockingIO {
    private static final Logger logger = LoggerFactory.getLogger(Part10NonBlockingIO.class);
    public static void main(String[] args) {

        var externalServiceClient = new ExternalServiceClient();

        logger.info("Start");
        for (int i = 1; i <=100; i++) {
            externalServiceClient.getProductName(i).subscribe(Util.getSubscriber());
        }
        logger.info("End");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
