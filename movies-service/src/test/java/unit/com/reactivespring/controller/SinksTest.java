package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinksTest {

    @Test
    void skin(){
        Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux = replaySink.asFlux();
        integerFlux.subscribe((i)->{
            System.out.println("Sub 1:" + i);
        });

        Flux<Integer> integerFlux1 = replaySink.asFlux();
        integerFlux1.subscribe((i)->{
            System.out.println("Sub 2:" + i);
        });

        replaySink.tryEmitNext(3);

    }

    @Test
    void sinks_multicast(){
        Sinks.Many<Integer> multicast = Sinks.many().multicast().onBackpressureBuffer();
        multicast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multicast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux = multicast.asFlux();
        integerFlux.subscribe((i)->{
            System.out.println("Sub 1:" + i);
        });

        Flux<Integer> integerFlux1 = multicast.asFlux();
        integerFlux1.subscribe((i)->{
            System.out.println("Sub 2:" + i);
        });
        multicast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

    }

    @Test
    void sinks_unicast(){
        Sinks.Many<Integer> multicast = Sinks.many().unicast().onBackpressureBuffer();
        multicast.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
        multicast.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux = multicast.asFlux();
        integerFlux.subscribe((i)->{
            System.out.println("Sub 1:" + i);
        });

        Flux<Integer> integerFlux1 = multicast.asFlux();
        integerFlux1.subscribe((i)->{
            System.out.println("Sub 2:" + i);
        });
        multicast.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

    }

}
