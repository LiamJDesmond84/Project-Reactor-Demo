package com.liam.projectreactor.models;

import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
	
    private MovieInfo movie;
    private List<Review> reviewList;
    private Revenue revenue;

    public Movie(MovieInfo movie, List<Review> reviewList) {
        this.movie = movie;
        this.reviewList = reviewList;
    }

}
