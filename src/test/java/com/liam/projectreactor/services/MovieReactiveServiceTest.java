package com.liam.projectreactor.services;

//import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;

class MovieReactiveServiceTest {
	


	private MovieInfoService movieInfoService = new MovieInfoService();

	private ReviewService reviewService = new ReviewService();
	
	MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);

	@Test
	void GetAllMovies() {
		
		//given
		
		
		
		//when
		
		
		
		//then
	}

}
