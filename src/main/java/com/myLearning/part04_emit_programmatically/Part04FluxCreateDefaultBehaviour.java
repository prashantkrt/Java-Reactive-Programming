package com.myLearning.part04_emit_programmatically;

import com.myLearning.part01_intro.subscriber.SubscriberImpl;
import com.myLearning.part04_emit_programmatically.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
// default behavior
public class Part04FluxCreateDefaultBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(Part04FluxCreateDefaultBehaviour.class);

    public static void main(String[] args) {

        var subscriber = new SubscriberImpl();

        //sink.next(...) pushes 100 items immediately, without waiting for downstream demand.
        //Flux.create() by default ignores downstream demand unless you explicitly handle it.
        Flux.<String>create(sink->{
            for (int i = 0; i < 100 ; i++) {
                var name = Util.faker().name().firstName();
                logger.info("Generated name: {}", name); // will be logged immediately even we have cancelled the subscription
                sink.next(name);
            }
            sink.complete();
        }).subscribe(subscriber);

      subscriber.getSubscription().request(10);
//        subscriber.getSubscription().cancel();
    }
}
