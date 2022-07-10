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
		
		moviesInfoFlux.flatMap(movieInfo -> {
			
			Mono<List<Review>> review = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
			.collectList();
			
		});
		

		
	}
}
