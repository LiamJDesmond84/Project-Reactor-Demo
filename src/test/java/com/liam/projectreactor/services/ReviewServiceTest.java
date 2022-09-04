package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.liam.projectreactor.models.Review;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ReviewServiceTest {
	
	WebClient webClient = WebClient.builder() // creates instance of WebClient that automatically connects to this baseUrl
			.baseUrl("http://localhost:8080/movies")
			.build();
	
	ReviewService reviewService = new ReviewService(webClient);
	
	
	
	@Test
	void retrieveReviewsFlux_RestClient() {
		
		//given
		long movieInfoId = 1;
		
		//when
		Flux<Review> reviewFlux = reviewService.retrieveReviewsFlux_RestClient(movieInfoId);
		
		//then
		StepVerifier.create(reviewFlux)
		.assertNext(x -> assertEquals("Nolan is the real superhero", x.getComment()))
//		.expectNextCount(1)
		.verifyComplete();
		
	}

}
