package com.liam.projectreactor.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.liam.projectreactor.models.Movie;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
//import org.springframework.beans.factory.annotation.Autowired;

class MovieReactiveServiceTest {
	


	private MovieInfoService movieInfoService = new MovieInfoService();

	private ReviewService reviewService = new ReviewService();
	
	MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);

	@Test
	void GetAllMovies() {
		
		//given
		
		
		
		//when
		Flux<Movie> result = movieReactiveService.getAllMovies();
		
		
		//then
		StepVerifier.create(result)
		
		// assertNext(expectNext, actual)
		.assertNext(x -> {
			// name of the movie
			// reviewList
			assertEquals("Batman Begins", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.assertNext(x -> {
			// name of the movie
			// reviewList
			assertEquals("The Dark Knight", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.assertNext(x -> {
			// name of the movie
			// reviewList
			assertEquals("The Dark Knight Rises", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.verifyComplete();

		
	}

}
