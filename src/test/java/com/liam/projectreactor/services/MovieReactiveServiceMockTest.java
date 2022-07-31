package com.liam.projectreactor.services;


import static org.mockito.ArgumentMatchers.anyLong;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.liam.projectreactor.exceptions.MovieException;
import com.liam.projectreactor.models.Movie;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class MovieReactiveServiceMockTest {
	
	
	@Mock
	private MovieInfoService movieInfoService; // Will be injected into MovieReactiveService
	
	@Mock
	private ReviewService reviewService; // Will be injected into MovieReactiveService
	
	
	@InjectMocks
	MovieReactiveService movieReactiveService;  // MovieReactiveService instance
	
	
	@Test
	void GetAllMovies() {
		//given
		Mockito.when(movieInfoService.retrieveMoviesFlux()) // When this call happens...
			.thenCallRealMethod(); // The real method(retrieveMoviesFlux) will be called
		
		Mockito.when(reviewService.retrieveReviewsFlux(anyLong())) // When this call happens...
		.thenCallRealMethod(); // The real method(retrieveMoviesFlux) will be called
		
		//when
		Flux<Movie> moviesFlux = movieReactiveService.getAllMovies();
		
		//then
		StepVerifier.create(moviesFlux)
			.expectNextCount(3)
			.verifyComplete();
	}
	
	
	@Test
	void GetAllMovies_1_Error() {
		
		
		//given
		String errorMessage = "Exception occurred in ReviewService";
		
		Mockito.when(movieInfoService.retrieveMoviesFlux()) // When this call happens...
			.thenCallRealMethod(); // The real method(retrieveMoviesFlux) will be called
		
		Mockito.when(reviewService.retrieveReviewsFlux(anyLong())) // When this call happens...
		.thenThrow(new RuntimeException(errorMessage)); // We want to throw an Error
		
		//when
		Flux<Movie> moviesFlux = movieReactiveService.getAllMovies();
		
		//then
		StepVerifier.create(moviesFlux)
			.expectError(MovieException.class)
			.verify();
	}

}
