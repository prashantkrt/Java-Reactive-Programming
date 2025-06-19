package com.myLearning.part02;

import com.myLearning.part02.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
/*
 * To delay the execution using supplier / callable
 */

/*
public static <T> Mono<T> fromCallable(Callable<? extends T> callable)
 - Creates a lazy Mono (like fromSupplier)
 - Accepts a Callable<T> instead of a Supplier<T>
 - Allows throwing checked exceptions (IOException, etc.)

| Interface    | Returns a value | Can throw checked exception? | Method     |
|--------------|-----------------|------------------------------|------------|
| Supplier<T>  | Yes             | No                           | T get()    |
| Callable<T>  | Yes             | Yes                          | T call()   |

@FunctionalInterface
public interface Supplier<T> {
    T get(); // does NOT allow checked exceptions but can throw unchecked
}
Supplier<String> supplier = () -> {
    if (true) throw new RuntimeException("Something went wrong"); // ✅ OK  Can throw unchecked exceptions (RuntimeException)
    // if (true) throw new IOException("IO Failed"); // ❌ Compile error since it Cannot throw checked exceptions directly (compiler error)
    return "data";
};

@FunctionalInterface
public interface Callable<T> {
    T call() throws Exception; // ✅ Can throw checked or unchecked exceptions
}
Callable<String> callable = () -> {
    if (true) throw new IOException("IO problem");  // ✅ Allowed
    return "data";
};
 */

public class Part06MonoFromCallable {

    private final static Logger logger = LoggerFactory.getLogger(Part06MonoFromCallable.class);

    public static void main(String[] args) {

        Callable<String> fetchUserData = () -> {
            System.out.println("Fetching user data...");
            boolean throwChecked = false;
            boolean throwUnchecked = true;
            if (throwChecked) {
                // Checked Exception
                throw new IOException("Simulated checked exception: File not found!");
            }
            if (throwUnchecked) {
                // Unchecked Exception (Runtime)
                throw new NullPointerException("Simulated unchecked exception: Null data");
            }
            return "User: Rajat";
        };


        Callable<String> fetchUserDataFromDB = () -> {
            System.out.println("Fetching user data...");
            // Simulate failure condition
            boolean databaseDown = true;
            if (databaseDown) {
                throw new RuntimeException("Database connection failed!");
            }
            return "User: Prashant";
        };


        Mono<String> mono = Mono.fromCallable(fetchUserDataFromDB);

        System.out.println("Mono created. Now subscribing...");

        // Step 3: Subscribe to Mono
        mono.subscribe(
                value -> System.out.println("onNext: " + value),
                error -> System.err.println("onError: " + error.getMessage()),
                () -> System.out.println("onComplete")
        );


        // example 2, both are lazy loading but supplier does not throw exception checked exception like callable

        var list = List.of(1, 2, 3);
        //in supplier, we cannot throw checked exception so have to handle it else it gonna give complier error
        Mono.fromSupplier(() -> {
                    try {
                        return sum(list);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribe(Util.getSubscriber());

        // Should use Callable since sum() throws exception
        Mono.fromCallable(() -> sum(list))
                .subscribe(Util.getSubscriber());

    }

    private static int sum(List<Integer> list) throws Exception {
        logger.info("finding the sum of {}", list);
        return list.stream().mapToInt(a -> a).sum();
    }

}
