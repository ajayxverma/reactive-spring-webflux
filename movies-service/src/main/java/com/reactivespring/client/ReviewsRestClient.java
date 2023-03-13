package com.reactivespring.client;

import com.reactivespring.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Component
public class ReviewsRestClient {

    @Value("${restClient.reviewsUrl}")
    private String reviewsurl;
    private WebClient webClient;

    public ReviewsRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Review> retrieveReviews(String movieId) {

        var uri = UriComponentsBuilder.fromHttpUrl(reviewsurl)
                        .queryParam("movieInfoId", movieId)
                                .buildAndExpand()
                                        .toString();

        return webClient
                .get()
                .uri(uri, movieId)
                .retrieve()
                .bodyToFlux(Review.class)
                .log();
    }
}
