package com.learnreactiveprogramming.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        SpringApplication.run(FluxAndMonoGeneratorService.class, args);
    }


}
