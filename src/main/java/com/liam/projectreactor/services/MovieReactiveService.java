package com.liam.projectreactor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.liam.projectreactor.models.Movie;
import com.liam.projectreactor.models.MovieInfo;
import com.liam.projectreactor.models.Review;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MovieReactiveService {
	
	@Autowired
	private MovieInfoService movieInfoService;
	
	@Autowired
	private ReviewService reviewService;

	
	
	public Flux<Movie> getAllMovies() {
		
		Flux<MovieInfo> moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
		
		return moviesInfoFlux.flatMap(movieInfo -> {
			
			Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
			.collectList();
			
			return reviewsMono
					.map(reviewsList -> new Movie(movieInfo, reviewsList));
			
		});
		

		
	}
}
