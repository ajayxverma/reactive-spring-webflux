package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewNotFoundException;
import com.reactivespring.repository.ReviewReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ReviewHandler {

    @Autowired
    private Validator validator;

    private ReviewReactiveRepository reviewReactiveRepository;

    public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
        this.reviewReactiveRepository = reviewReactiveRepository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {

         return request.bodyToMono(Review.class)
                 .doOnNext(this::validate)
                .flatMap(reviewReactiveRepository::save)
                 .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
    }

    private void validate(Review review) {

        var constrainsVoilations = validator.validate(review);
        log.info("ConstrainVoilation : {} ", constrainsVoilations);

        if (constrainsVoilations.size() > 0) {
            var errorMessage = constrainsVoilations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));

            throw new ReviewNotFoundException(errorMessage);

        }

    }

    public Mono<ServerResponse> getReview(ServerRequest request) {

        var reviewsFlux = reviewReactiveRepository.findAll();

        return ServerResponse.ok().body(reviewsFlux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {

        var reviewid = request.pathVariable("id");
        var existingReview = reviewReactiveRepository.findById(reviewid);
        return existingReview
                .flatMap(review -> request.bodyToMono(Review.class)
                        .map(reqReveiw -> {
                            review.setComment(reqReveiw.getComment());
                            review.setReviewId(reqReveiw.getReviewId());
                            return review;
                        })
                        .flatMap(reviewReactiveRepository::save)
                        .flatMap(saveReview -> ServerResponse.ok().bodyValue(saveReview))
                );
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {

        var reviewId = request.pathVariable("id");
        var exisitingReview = reviewReactiveRepository.findById(reviewId);

        return exisitingReview
                .flatMap(review -> reviewReactiveRepository.deleteById(reviewId)
                        .then(ServerResponse.noContent().build()));

    }
}
