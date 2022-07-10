package com.liam.projectreactor.services;

import java.util.List;

import com.liam.projectreactor.models.Review;

import reactor.core.publisher.Flux;

public class ReviewService {
	
	public  List<Review> retrieveReviews(long movieInfoId){

        return List.of(
        		new Review(1L, movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
    }

    public Flux<Review> retrieveReviewsFlux(long movieInfoId){

        List<Review> reviewsList = List.of(
        		new Review(1L,movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
        
        return Flux.fromIterable(reviewsList);
    }

}
