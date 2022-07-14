package com.liam.projectreactor.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.jupiter.api.Test;

import com.liam.projectreactor.models.Movie;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
		
		
		.assertNext(x -> {
			// name of the movie
			// reviewList
			
			// assertEquals(expectNext, actual)
			assertEquals("Batman Begins", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.assertNext(x -> {
			// name of the movie
			// reviewList
			
			// assertEquals(expectNext, actual)
			assertEquals("The Dark Knight", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.assertNext(x -> {
			// name of the movie
			// reviewList
			
			// assertEquals(expectNext, actual)
			assertEquals("The Dark Knight Rises", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.verifyComplete();

		
	}
	
	
	@Test
	void getMovieByIdFlatMap() {
		
		
		///////
		/////// GO OVER THIS ONE AGAIN
		///////
		
		//given
		long movieId = 100L;
		
		
		//when

		Mono<Movie> result = movieReactiveService.getMovieByIdFlatMap(movieId)
				.log();
		
		
		//then
		StepVerifier.create(result)
		
		.assertNext(x -> {
			// name of the movie
			// reviewList
			
			// assertEquals(expectNext, actual)
			assertEquals("Batman Begins", x.getMovie().getName());
			assertEquals(2, x.getReviewList().size());
			

		})
		.verifyComplete();
		
	}

}
