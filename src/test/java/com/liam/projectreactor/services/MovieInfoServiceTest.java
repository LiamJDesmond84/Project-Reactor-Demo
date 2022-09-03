package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.liam.projectreactor.models.MovieInfo;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MovieInfoServiceTest {
	
	

	
	WebClient webClient = WebClient.builder() // creates instance of WebClient that automatically connects to this baseUrl
							.baseUrl("http://localhost:8080/movies")
							.build();
	
	
	MovieInfoService movieInfoService = new MovieInfoService(webClient);
	
	@Test
	void retrieveAllMovieInfo_RestClient() {
		
		//given
		
		
		//when
		Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveAllMovieInfo_RestClient();
		
		//then
		StepVerifier.create(movieInfoFlux)
			.expectNextCount(7)
			.verifyComplete();
		
	}

}
