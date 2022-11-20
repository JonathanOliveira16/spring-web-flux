package com.reactivespring.controller;

import com.learnreactiveprogramming.service.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("local")
@AutoConfigureWebClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(properties = {
        "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
        "restClient.reviewsUrl=http://localhost:8084/v1/reviews"
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesControllerIntgTest {


    @Autowired
    WebTestClient webTestClient;

    @Test
    void retrieveMovieById(){
        var movieId = "abc";
        stubFor(get(urlEqualTo("/v1/moviesinfos"+ "/"+movieId))
        .willReturn(aResponse()
                .withHeader("Content-Type", "aplication/json")
                .withBodyFile("movieinfo.json")
        ));
        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "aplication/json")
                        .withBodyFile("reviews.json")
                ));
        webTestClient.get().uri("/v1/movies/{id}").exchange().expectStatus().isOk().expectBody(Movie.class)
        .consumeWith(movieEntityExchangeResult -> {
            var movie = movieEntityExchangeResult.getResponseBody();
            assertEquals("Batman Begins", movie.getMovieInfo().getName());
        });


    }

}
