package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;

import com.liam.projectreactor.models.Review;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ReviewServiceTest {
	
	
	ReviewService reviewService = new ReviewService();
	
	
	
	@Test
	void retrieveReviewsFlux_RestClient() {
		
		//given
		long movieInfoId = 1;
		
		//when
		Flux<Review> reviewFlux = reviewService.retrieveReviewsFlux_RestClient(movieInfoId);
		
		//then
		StepVerifier.create(reviewFlux)
		.verifyComplete();
		
		
	}

}
