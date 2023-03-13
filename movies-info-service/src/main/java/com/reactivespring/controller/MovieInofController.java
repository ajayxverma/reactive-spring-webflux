package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoServices;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class MovieInofController {


    private MovieInfoServices movieInfoServices;

    public MovieInofController(MovieInfoServices movieInfoServices) {
        this.movieInfoServices = movieInfoServices;
    }
    @GetMapping("/movieinfos/{id}")
    public Mono<MovieInfo> getAllMovieInfoById(@PathVariable String id) {

        return movieInfoServices.getAllMovieInfoById(id).log();

    }
    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMovieInfos() {

        return movieInfoServices.getAllMovieInfos().log();

    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInof(@RequestBody @Valid MovieInfo movieInfo) {
            return movieInfoServices.addMovies(movieInfo).log();
    }
    @PutMapping("/movieinfos/{id}")
    public Mono<MovieInfo> updateMovieInof(@RequestBody MovieInfo updatedmovieInfo, @PathVariable String id) {
        return movieInfoServices.updateMovieInfo(updatedmovieInfo, id);
    }


}
