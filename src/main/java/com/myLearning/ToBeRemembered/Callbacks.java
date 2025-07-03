package com.myLearning.ToBeRemembered;

interface OrderCallback {
    void onOrderReady(String message);
}

class Restaurant {
    public void placeOrder(OrderCallback callback) {
        System.out.println("Order placed... please wait");
        callback.onOrderReady("Your order is ready");
    }
}

public class Callbacks {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();

        // Passing the callback implementation
        restaurant.placeOrder(message -> {
            System.out.println("Callback received: " + message);
        });
    }
}
