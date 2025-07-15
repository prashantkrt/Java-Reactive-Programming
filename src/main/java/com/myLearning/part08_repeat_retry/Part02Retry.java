package com.myLearning.part08_repeat_retry;

import reactor.core.publisher.Flux;


//retry() is for error recovery cycles.

/*
[Source Flux/Mono]
     |
     v
[Emits Data]
     |
     v
[If ERROR occurs]
     |
     v
[retry() kicks in]
     |
     v
[Resubscribe to Source Flux]
     |
     v
[Repeat until successful OR retry limit exceeded]
 */
public class Part02Retry {
    public static void main(String[] args) {
        Flux<Integer> flux = Flux.range(1, 3)
                .map(i -> {
                    if (i == 2) throw new RuntimeException("Error at: " + i);
                    return i;
                })
                .retry(2);  // retry 2 times on error

        flux.subscribe(
                val -> System.out.println("Received: " + val),
                err -> System.out.println("Error after retries: " + err.getMessage())
        );
    }
}

/*
➡️ First Subscription:
  Processing 1 ✅
  Processing 2 ❌ -> Error happens

➡️ Retry 1:
  Processing 1 ✅
  Processing 2 ❌ -> Error happens again

➡️ Retry 2:
  Processing 1 ✅
  Processing 2 ❌ -> Error happens again

➡️ Retries exhausted ❌
➡️ Final Error propagated to subscriber.
 */
