package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    @Autowired
    private MoviesInfoService moviesInfoService;

    Sinks.Many<MovieInfo> moviesInfoSink = Sinks.many().replay().latest();

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMoviesInfo(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value="name", required = false) String name){
        if(year!=null){
            return moviesInfoService.getMovieInfoByYear(year);
        }
        if(name!=null){
            return moviesInfoService.getMovieInfoByName(name);
        }
        return moviesInfoService.getAllMoviesInfos().log();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMoviesInfoById(@PathVariable String id){
        return moviesInfoService.getAllMoviesInfoById(id).map(movieInfo1 -> ResponseEntity.ok().body(movieInfo1))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @GetMapping(value = "/movieinfos/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MovieInfo> getMoviesInfoById(){
        return moviesInfoSink.asFlux();
    }


    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo){
        return moviesInfoService.addMovieInfo(movieInfo)
                .doOnNext(savedInfo -> moviesInfoSink.tryEmitNext(savedInfo));


    }

    @PutMapping("/movieinfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updatedmovieInfo, @PathVariable String id){
        return moviesInfoService.updateMovieInfo(updatedmovieInfo, id).map(movieInfo -> {
            return ResponseEntity.ok().body(movieInfo);
        }).switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @DeleteMapping("/movieinfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id){
        return moviesInfoService.deleteMovieInfo(id);
    }
}
