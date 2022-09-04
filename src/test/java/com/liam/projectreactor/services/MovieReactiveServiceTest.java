package com.liam.projectreactor.services;

import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.jupiter.api.Test;

import com.liam.projectreactor.models.Movie;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
//import org.springframework.beans.factory.annotation.Autowired;

class MovieReactiveServiceTest {
	


	private MovieInfoService movieInfoService = new MovieInfoService();

	private ReviewService reviewService = new ReviewService();
	
	private RevenueService revenueService = new RevenueService();
	
//	MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);
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
			assertNotNull(x.getRevenue());
			

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
	
	
	@Test
	void getMovieByIdWithRevenue() {
		
		//given
		long movieId = 100L;
		
		//when
		Mono<Movie> movieMono = movieReactiveService.getMovieByIdWithRevenue(movieId)
				.log();
		
		//then
		StepVerifier.create(movieMono)
			.assertNext(x -> {
				assertEquals("Batman Begins", x.getMovie().getName());
				assertEquals(2, x.getReviewList().size());
			})
			.verifyComplete();
	}
	

//    ___        ___       
//   /   \___   /___\_ __  
//  / /\ / _ \ //  // '_ \ 
// / /_// (_) / \_//| | | |
///___,' \___/\___/ |_| |_|
//                         

}
