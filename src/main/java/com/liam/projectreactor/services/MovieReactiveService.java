package com.liam.projectreactor.services;

import java.util.List;



import com.liam.projectreactor.models.Movie;
import com.liam.projectreactor.models.MovieInfo;
import com.liam.projectreactor.models.Review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
					.map(reviewsListVar -> new Movie(movieInfoVar, reviewsListVar))
					.log();
			
		});
			
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
