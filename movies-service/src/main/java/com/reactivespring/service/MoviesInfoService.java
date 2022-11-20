package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMoviesInfos() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getAllMoviesInfoById(String id) {
        return movieInfoRepository.findById(id).log();
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo updatedmovieInfo, String id) {
        return movieInfoRepository.findById(id).flatMap(movieInfo -> {
             movieInfo.setCast(updatedmovieInfo.getCast());
             movieInfo.setName(updatedmovieInfo.getName());
             movieInfo.setRelease_date(updatedmovieInfo.getRelease_date());
             movieInfo.setYear(updatedmovieInfo.getYear());
             return movieInfoRepository.save(movieInfo);
         });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Flux<MovieInfo> getMovieInfoByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }

    public Flux<MovieInfo> getMovieInfoByName(String name) {
        return movieInfoRepository.findByName(name);
    }
}
