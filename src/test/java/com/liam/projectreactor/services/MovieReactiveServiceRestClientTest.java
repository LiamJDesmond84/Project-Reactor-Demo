package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.liam.projectreactor.models.MovieInfo;
import com.liam.projectreactor.models.Review;

import reactor.core.publisher.Flux;

public class MovieReactiveServiceRestClientTest {
	
	
	WebClient webClient = WebClient.builder() // creates instance of WebClient that automatically connects to this baseUrl
			.baseUrl("http://localhost:8080/movies")
			.build();
	


	private MovieInfoService movieInfoService = new MovieInfoService(webClient);
	
	private ReviewService reviewService = new ReviewService(webClient);
	
	

	@Test
	void getAllMovies_restClient() {
		
		
		//given
		long movieInfoId = 1;
		
		//when
		Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();
		
		Flux<Review> reviewFlux = reviewService.retrieveReviewsFlux_RestClient(movieInfoId);
		
		//then
		@Step
	}
	

}
