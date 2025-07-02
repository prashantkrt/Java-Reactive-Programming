package com.myLearning.part03.subscriber_stock;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockPriceObserver implements Subscriber<Integer> {

    Logger logger = LoggerFactory.getLogger(StockPriceObserver.class);
    private Subscription subscription;
    private Integer quantity=0; //counting for the number of stock purchased
    private Integer balance=1000; // my account balance

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Integer.MAX_VALUE);
        this.subscription = subscription;
    }

    @Override
    public void onNext(Integer price) {
        // condition check for buying price should be 90 to purchase the stock
        // sell the stock when the price is greater than 110
        // for selling we will be selling all the stocks quantity at once and getting the profit
        if(price< 90 && balance>=price) {
            balance -= price;
            quantity++;
            logger.info("Buying stock at price {} with quantity {}", price,quantity);
        }
        else if(price>110 && quantity>0) {
            balance = balance + (quantity * price);
            quantity=0;
            logger.info("Selling stock at price {} and getting profit {}", price,balance-1000);
        }
    }

    @Override
    public void onError(Throwable throwable) {
       logger.error(throwable.getMessage(), throwable);
    }

    @Override
    public void onComplete() {
        logger.info("completed");
    }
}
