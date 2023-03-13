package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Service
public class MovieInfoServices {


    private MovieInfoRepository movieInfoRepository;

    public MovieInfoServices(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovies(MovieInfo movieInfo) {

        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMovieInfos() {

        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getAllMovieInfoById(String id) {

        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo updatedmovieInfo, String id) {

        return movieInfoRepository.findById(id)
                .flatMap(movieinfo -> {
                    movieinfo.setCast(updatedmovieInfo.getCast());
                    movieinfo.setName(updatedmovieInfo.getName());
                    movieinfo.setRelease_date(updatedmovieInfo.getRelease_date());
                    movieinfo.setYear(updatedmovieInfo.getYear());

                    return movieInfoRepository.save(movieinfo);
                });
    }
}
