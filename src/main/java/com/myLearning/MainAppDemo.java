package com.myLearning;

/*
   Rules of Reactive Streams
   1. publisher does not produce data unless subscriber requests for it.
   2. publisher will produce only <= subscriber-requested items. Publisher can also produce 0 items!
   3. subscriber can cancel the subscription. producer should stop at that moment as subscriber is no longer interested in consuming the data
   4. producer can send the error signal
 */

import com.myLearning.part01.publisher.PublisherImpl;
import com.myLearning.part01.subscriber.SubscriberImpl;

import java.time.Duration;

public class MainAppDemo
{
    public static void main( String[] args )
    {
        demo1();
        System.out.println("*******************************");

        try {
            demo2();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("*******************************");


        try {
            demo3();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("*******************************");

        try {
            demo4();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("*******************************");

    }

    public static void demo1(){
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);
    }

    // for getting no more data to send and completing the subscription
    public static void demo2() throws InterruptedException{
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);

        // request for 3 items
        subscriber.getSubscription().request(3);
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println("----------------------------");

        // request for 4 items
        subscriber.getSubscription().request(4);
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println("----------------------------");

        // request for 2 items
        subscriber.getSubscription().request(2);
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println("----------------------------");

        // request for 3 items
        subscriber.getSubscription().request(3);
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println("----------------------------");

        // request for 3 items
        subscriber.getSubscription().request(3);
    }

    // for canceling the subscription by subscriber in between
    private static void demo3() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);

        subscriber.getSubscription().request(3);
        Thread.sleep(Duration.ofSeconds(2));
        subscriber.getSubscription().cancel();
        System.out.println("----------------------------");

        subscriber.getSubscription().request(3);
        Thread.sleep(Duration.ofSeconds(2));
    }

    // for getting the error for max requested items
    private static void demo4() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber = new SubscriberImpl();
        publisher.subscribe(subscriber);

        subscriber.getSubscription().request(3);
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println("----------------------------");

        subscriber.getSubscription().request(11);
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println("----------------------------");

        subscriber.getSubscription().request(3);
        Thread.sleep(Duration.ofSeconds(2));
    }
}
