package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class MovieInfoServiceTest {
	
	

	
	WebClient webClient = WebClient.builder() // creates instance of WebClient that automatically connects to this baseUrl
							.baseUrl("http://localhost:8080/movies")
							.build();
	
	
	MovieInfoService movieInfoService = new MovieInfoService(webClient);
	
	@Test
	void retrieveAllMovieInfo_RestClient() {
		
		//given
		
		
		//when
		
		
		//then
		
		
	}

}
