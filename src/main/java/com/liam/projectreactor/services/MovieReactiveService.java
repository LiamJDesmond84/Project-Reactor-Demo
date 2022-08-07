package com.liam.projectreactor.services;

import java.time.Duration;
import java.util.List;

import com.liam.projectreactor.exceptions.MovieException;
import com.liam.projectreactor.exceptions.NetworkException;
import com.liam.projectreactor.exceptions.ServiceException;
import com.liam.projectreactor.models.Movie;
import com.liam.projectreactor.models.MovieInfo;
import com.liam.projectreactor.models.Review;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Slf4j
public class MovieReactiveService {
	

	private MovieInfoService movieInfoService;
	
	private ReviewService reviewService;
	
//	public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
//		this.movieInfoService = movieInfoService;
//		this.reviewService = reviewService;
//	}

	
	
	public Flux<Movie> getAllMovies() {
		
		Flux<MovieInfo> moviesInfoFlux = movieInfoService.retrieveMoviesFlux(); // Retrieving List of MovieInfo - But we want the ID in order to pull the list of reviews
		
		System.out.println(moviesInfoFlux);
		

		return moviesInfoFlux
				// flatMap because "reviewService.retrieveReviewsFlux" returns a Reactive type(Flux)
				// Using flatMap we are passing Movie ID & retrieving the Reviews
			.flatMap(movieInfoVar -> {
				// collectList gives a Mono, but the Reviews are represented as a List
				Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfoVar.getMovieInfoId())
				// Collecting to list because Movie class has List<Review>
			.collectList();
			
		System.out.println(moviesInfoFlux);	
		System.out.println(reviewsMono);

				// Usings reviewsMono to map & build a new Movie with MovieInfo(moviesInfoFlux- > movieInfoVar) & List<Review>(reviewsMono -> reviewsListVar)
			return reviewsMono
					.map(reviewsListVar -> new Movie(movieInfoVar, reviewsListVar));
			})
			.onErrorMap((exc) -> { // Exception handler
				log.error("The EXCEPTION is......... ", exc);
				throw new MovieException(exc.getMessage());
			})
			.log();
			
	}

//			_              
// _ __ ___| |_ _ __ _   _ 
//| '__/ _ \ __| '__| | | |
//| | |  __/ |_| |  | |_| |
//|_|  \___|\__|_|   \__, |
//             		|___/ 	
	
	public Flux<Movie> getAllMovies_Retry() {
		
		Flux<MovieInfo> moviesInfoFlux = movieInfoService.retrieveMoviesFlux(); // Retrieving List of MovieInfo - But we want the ID in order to pull the list of reviews
		
		System.out.println(moviesInfoFlux);
		

		return moviesInfoFlux
				// flatMap because "reviewService.retrieveReviewsFlux" returns a Reactive type(Flux)
				// Using flatMap we are passing Movie ID & retrieving the Reviews
			.flatMap(movieInfoVar -> {
				// collectList gives a Mono, but the Reviews are represented as a List
				Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfoVar.getMovieInfoId())
				// Collecting to list because Movie class has List<Review>
			.collectList();
			
		System.out.println(moviesInfoFlux);	
		System.out.println(reviewsMono);

				// Usings reviewsMono to map & build a new Movie with MovieInfo(moviesInfoFlux- > movieInfoVar) & List<Review>(reviewsMono -> reviewsListVar)
			return reviewsMono
					.map(reviewsListVar -> new Movie(movieInfoVar, reviewsListVar));
			})
			.onErrorMap((exc) -> { // Exception handler
				log.error("The EXCEPTION is......... ", exc);
				throw new MovieException(exc.getMessage());
			})
			.retry(3) // Retries FOREVER unless specified
			.log();
			
	}
	
	
	

//    	    _              __    __ _                
// _ __ ___| |_ _ __ _   _/ / /\ \ \ |__   ___ _ __  
//| '__/ _ \ __| '__| | | \ \/  \/ / '_ \ / _ \ '_ \ 
//| | |  __/ |_| |  | |_| |\  /\  /| | | |  __/ | | |  ---  Set a retry amount with a Duration or a condition
//|_|  \___|\__|_|   \__, | \/  \/ |_| |_|\___|_| |_|
//             		 |___/                           	
	
	public Flux<Movie> getAllMovies_RetryWhen() {
		
//		Retry retryWhenVar = Retry.backoff(3, Duration.ofMillis(500)) // Setting a retry amount with a Duration
//				.onRetryExhaustedThrow((retryBackOffSpec, retrySignal) -> 
//					Exceptions.propagate(retrySignal.failure())
//				); 
		
		Retry retryWhenVar = getRetryBackOffFunction(); // Extracted whole code block(below) to simple function name.

		
		Flux<MovieInfo> moviesInfoFlux = movieInfoService.retrieveMoviesFlux(); // Retrieving List of MovieInfo - But we want the ID in order to pull the list of reviews
		
		System.out.println(moviesInfoFlux);
		

		return moviesInfoFlux
				// flatMap because "reviewService.retrieveReviewsFlux" returns a Reactive type(Flux)
				// Using flatMap we are passing Movie ID & retrieving the Reviews
			.flatMap(movieInfoVar -> {
				// collectList gives a Mono, but the Reviews are represented as a List
				Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfoVar.getMovieInfoId())
				// Collecting to list because Movie class has List<Review>
			.collectList();
			
		System.out.println(moviesInfoFlux);	
		System.out.println(reviewsMono);

				// Usings reviewsMono to map & build a new Movie with MovieInfo(moviesInfoFlux- > movieInfoVar) & List<Review>(reviewsMono -> reviewsListVar)
			return reviewsMono
					.map(reviewsListVar -> new Movie(movieInfoVar, reviewsListVar));
			})
			.onErrorMap((exc) -> { // Exception handler
				log.error("The EXCEPTION is......... ", exc);
				if(exc instanceof NetworkException) {
					throw new MovieException(exc.getMessage());
				}
				else {
					throw new ServiceException(exc.getMessage());
				}
			})
//			.retryWhen(retryWhenVar) // Using retry amount with a Duration
			.retryWhen(getRetryBackOffFunction()) // Using retry amount with a Duration - Extracted function works as well
			.log();
			
	}

	private Retry getRetryBackOffFunction() {
		Retry retryWhenVar = Retry.backoff(3, Duration.ofMillis(500)) // Setting a retry amount with a Duration with a filter/predicate
				.filter(ex -> ex instanceof MovieException) // ONLY perform the retry if it's this exception(MovieException)
				.onRetryExhaustedThrow((retryBackOffSpec, retrySignal) -> 
					Exceptions.propagate(retrySignal.failure())
				);
		return retryWhenVar;
	}
	
	
	
	
	
	
	
	
	
	// Mono - Because it's just ONE movie
	public Mono<Movie> getMovieById(long movieId) {
		
		Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
		
		Mono<List<Review>> reviewsMonoList = reviewService.retrieveReviewsFlux(movieId)
				.collectList();
		
		return movieInfoMono.zipWith(reviewsMonoList, (movieInf, rev) -> new Movie(movieInf, rev));
		
	}
	
	
	// Mono - Because it's just ONE movie - ASSIGNMENT
	public Mono<Movie> getMovieByIdFlatMap(long movieId) {
		
		Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
		
		Mono<List<Review>> reviewsMonoList = reviewService.retrieveReviewsFlux(movieId)
				.collectList();
		
		return movieInfoMono.flatMap(movieInfoVar -> {
			// collectList gives a Mono, but the Reviews are represented as a List
			Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfoVar.getMovieInfoId())
			// Collecting to list because Movie class has List<Review>
		.collectList();
		
	System.out.println(movieInfoMono);	
	System.out.println(reviewsMonoList);

			// Usings reviewsMono to map & build a new Movie with MovieInfo(moviesInfoFlux- > movieInfoVar) & List<Review>(reviewsMono -> reviewsListVar)
		return reviewsMono
				.map(reviewsListVar -> new Movie(movieInfoVar, reviewsListVar))
				.log();
		
	});
		
	}
	
	
	
}
