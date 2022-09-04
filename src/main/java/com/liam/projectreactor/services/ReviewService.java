package com.liam.projectreactor.services;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import com.liam.projectreactor.models.MovieInfo;
import com.liam.projectreactor.models.Review;

import reactor.core.publisher.Flux;

public class ReviewService {
	
	private WebClient webClient;
	
	public ReviewService(WebClient webClient) {
		this.webClient = webClient;
	}
	
	
	public ReviewService() { // resolves compilation issues with testcases
		
	}
	
	
	public Flux<Review> retrieveReviewsFlux_RestClient(long movieInfoId) {
		
		return webClient
				.get()
				.uri("/v1/movie_infos")
				.retrieve()
				.bodyToFlux(Review.class)
				.log();
	}
	
//     _        _   _      
// ___| |_ __ _| |_(_) ___ 
/// __| __/ _` | __| |/ __|
//\__ \ || (_| | |_| | (__ 
//|___/\__\__,_|\__|_|\___|
//  
	
	public  List<Review> retrieveReviews(long movieInfoId){

        return List.of(
        		new Review(1L, movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
    }

    public Flux<Review> retrieveReviewsFlux(long movieInfoId){

        List<Review> reviewsList = List.of(
        		new Review(1L,movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
        
        return Flux.fromIterable(reviewsList);
    }

}
