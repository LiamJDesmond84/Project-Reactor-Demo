package com.liam.projectreactor.services;

import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


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
		
		String uri = UriComponentsBuilder.fromUriString("/v1/reviews")
				.queryParam("movieInfoId", movieInfoId) // Because query params can be complicated with these
				.buildAndExpand()
				.toUriString();
		
		return webClient
				.get()
				.uri(uri)
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
