package com.myLearning.part03;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Convert Mono<T> ➝ Flux<T>
// Convert Flux<T> ➝ Mono<T>
public class Part11FluxToMono {
    public static void main(String[] args) {

        // Flux to Mono examples
        Flux<String> stringFlux = Flux.just("Apple", "Banana", "Cherry");
        Mono<String> stringMonoFromFlux = toMono(stringFlux);
        stringMonoFromFlux.subscribe(s -> System.out.println("First string from flux: " + s));

        Flux<Integer> intFlux = Flux.just(10, 20, 30);
        Mono<Integer> stringMonoFromIntFlux = toMonoInteger(intFlux);
        stringMonoFromIntFlux.subscribe(i -> System.out.println("First integer from flux: " + i));

        // Mono to Flux examples
        Mono<String> stringMono = Mono.just("Hello Mono");
        Flux<String> stringFluxFromMono = toFlux(stringMono);
        stringFluxFromMono.subscribe(s -> System.out.println("Mono to Flux (String): " + s));

        Mono<Integer> intMono = Mono.just(99);
        Flux<Integer> intFluxFromMono = toFluxInteger(intMono);
        intFluxFromMono.subscribe(i -> System.out.println("Mono to Flux (Integer): " + i));



        //Output:
        //First string from flux: Apple
        //First integer from flux: 10
        //Mono to Flux (String): Hello Mono
        //Mono to Flux (Integer): 99


        //using from to convert Flux to Mono and Mono to Flux
        /*
         Mono<String> mono = Mono.just("Hello Mono");
         Flux<String> flux = Flux.from(mono);
         flux.subscribe(System.out::println);
         // Output: Hello Mono

         Flux<String> flux = Flux.just("A", "B", "C");
         Mono<String> mono = Mono.from(flux);
         mono.subscribe(System.out::println);
         // Output: A

         */

        // Example Mono
        Mono<String> mono = Mono.just("Hello from Mono");

        // Example Flux
        Flux<String> flux = Flux.just("A", "B", "C");

        // Convert Mono to Flux
        Flux<String> resultFlux = convertMonoToFluxUsingFrom(mono);
        resultFlux.subscribe(val -> System.out.println("Flux value: " + val));
        // Flux value: Hello from Mono

        // Convert Flux to Mono
        System.out.println("Flux to Mono using Mono.from():");
        Mono<String> resultMono = convertFluxToMonoUsingFrom(flux);
        resultMono.subscribe(val -> System.out.println("Mono value: " + val));
        //Mono value: A

    }

    /**
     * Converts a Flux<String> to Mono<String>
     * Emits only the first element from the Flux.
     */
    public static Mono<String> toMono(Flux<String> flux) {
        return flux.next();  // Returns Mono containing first item of the flux (or empty if none)
    }

    /**
     * Converts a Flux<Integer> to Mono<Integer>
     * Emits only the first integer from the Flux.
     */
    public static Mono<Integer> toMonoInteger(Flux<Integer> flux) {
        return flux.next();  // Again, gets only the first item from the Flux
    }

    /**
     * Converts a Mono<String> to Flux<String>
     * Will emit 0 or 1 item as a Flux.
     */
    public static Flux<String> toFlux(Mono<String> mono) {
        return mono.flux();  // Converts Mono into a Flux with up to one element
    }

    /**
     * Converts a Mono<Integer> to Flux<Integer>
     * Will emit 0 or 1 integer in a Flux stream.
     */
    public static Flux<Integer> toFluxInteger(Mono<Integer> mono) {
        return mono.flux();  // Converts Mono into Flux for integer value
    }

    // Convert Mono<T> to Flux<T> using Flux.from()
    public static <T> Flux<T> convertMonoToFluxUsingFrom(Mono<T> mono) {
        return Flux.from(mono);
    }

    //  Convert Flux<T> to Mono<T> using Mono.from()
    public static <T> Mono<T> convertFluxToMonoUsingFrom(Flux<T> flux) {
        return Mono.from(flux);
    }
}

