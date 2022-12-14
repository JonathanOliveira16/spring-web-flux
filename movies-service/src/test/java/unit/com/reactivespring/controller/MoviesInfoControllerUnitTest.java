package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MoviesInfoService moviesInfoService;

    static String MOVIES_INFO_URL = "/v1/movieinfos";

    @Test
    void getAllMoviesInfo(){
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
        when(moviesInfoService.getAllMoviesInfos()).thenReturn(Flux.fromIterable(movieinfos));
        webTestClient.get().uri(MOVIES_INFO_URL).exchange().expectStatus().is2xxSuccessful().expectBodyList(MovieInfo.class).hasSize(3);

    }

    @Test
    void getMoviesInfoById() {
        var movieinfos = new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));
        var movieInfoId = "abc";
        when(moviesInfoService.getAllMoviesInfoById(movieInfoId)).thenReturn(Mono.just(movieinfos));

        webTestClient.get().uri(MOVIES_INFO_URL+"/{id}", movieInfoId).exchange().expectStatus().is2xxSuccessful()
                .expectBody().jsonPath("$.name").isEqualTo("Dark Knight Rises");
               /* .expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
                  var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
                  assertNotNull(movieInfo);
        });*/
    }


    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null, "Batman Begins12",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(Mono.just( new MovieInfo("mockId", "Batman Begins12",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));
        webTestClient.post().uri(MOVIES_INFO_URL).bodyValue(movieInfo).exchange().expectStatus().isCreated().expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var saveMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert saveMovieInfo!= null;
                    assert saveMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId", saveMovieInfo.getMovieInfoId());
                });
    }

    @Test
    void updateMovieInfo() {
        var movieInfoId = "abc";
        var movieInfo = new MovieInfo(null, "Dark Knight Rises 2222",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        when(moviesInfoService.updateMovieInfo(isA(MovieInfo.class), isA(String.class))).thenReturn(Mono.just( new MovieInfo("mockId", "Batman Begins12",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"))));
        webTestClient.put().uri(MOVIES_INFO_URL+"/{id}", movieInfoId).bodyValue(movieInfo).exchange().expectStatus().is2xxSuccessful().expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert updatedMovieInfo!= null;
                    assert updatedMovieInfo.getMovieInfoId() != null;
                    assertEquals("Batman Begins12", updatedMovieInfo.getName());
                });
    }

    @Test
    void addMovieInfo_validation() {
        var movieInfo = new MovieInfo(null, "",
                -2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        webTestClient.post().uri(MOVIES_INFO_URL).bodyValue(movieInfo).exchange().expectStatus().isBadRequest().expectBody(String.class)
        .consumeWith(stringEntityExchangeResult -> {
            var response = stringEntityExchangeResult.getResponseBody();
            System.out.println(response);
            var expectedMessage = "movieInfo.name must be present,movieInfo.year must be a Positive Value";
            assert response!= null;
            assertEquals(expectedMessage, response);
        });/*.expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var saveMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert saveMovieInfo!= null;
                    assert saveMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId", saveMovieInfo.getMovieInfoId());
                });*/
    }

}
