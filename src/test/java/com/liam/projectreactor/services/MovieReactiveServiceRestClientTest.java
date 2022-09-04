package com.liam.projectreactor.services;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.web.reactive.function.client.WebClient;

public class MovieReactiveServiceRestClientTest {
	
	
	WebClient webClient = WebClient.builder() // creates instance of WebClient that automatically connects to this baseUrl
			.baseUrl("http://localhost:8080/movies")
			.build();
	


	private MovieInfoService movieInfoService = new MovieInfoService(webClient);
	
	private ReviewService reviewService = new ReviewService(webClient);
	
	


	

}
