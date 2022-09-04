package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.liam.projectreactor.models.Movie;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MovieReactiveServiceRestClientTest {
	
	
	WebClient webClient = WebClient.builder() // creates instance of WebClient that automatically connects to this baseUrl
			.baseUrl("http://localhost:8080/movies")
			.build();
	


	private MovieInfoService movieInfoService = new MovieInfoService(webClient);
	
	private ReviewService reviewService = new ReviewService(webClient);
	
	private MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);
	

	@Test
	void getAllMovies_restClient() {
		
		
		//given
		
		
		//when
		
		Flux<Movie> moviesFlux = movieReactiveService.getAllMovies_restClient();
		
		//then
		StepVerifier.create(moviesFlux)
			.expectNextCount(7)
			.verifyComplete();
	}
	
	
	
	@Test
	void getMovieById_restClient() {
		
		//given
		long movieId = 4;
		
		//when
		
		Mono<Movie> moviesFlux = movieReactiveService.getMovieById_restClient(movieId);
		
		//then
		StepVerifier.create(moviesFlux)
			.expectNextCount(1)
			.verifyComplete();
	}
		
		
	
	

}
