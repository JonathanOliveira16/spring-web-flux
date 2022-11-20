package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux(){
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        StepVerifier.create(namesFlux)
                //.expectNext("alex","bean","chloe")
                .expectNext("alex")
        .expectNextCount(2).verifyComplete();
    }

    @Test
    void namesFlux_map() {
        int stringLenght = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLenght);
        StepVerifier.create(namesFlux).expectNext("ALEX", "BEAN", "CHLOE").verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();
        StepVerifier.create(namesFlux).expectNext("alex","bean","chloe").verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {
        int stringLenght = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLenght);
        StepVerifier.create(namesFlux).expectNext("A","L","E","X","C","H","L","O","E").verifyComplete();
    }
}