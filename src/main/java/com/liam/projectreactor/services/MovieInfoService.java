package com.liam.projectreactor.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import static com.liam.projectreactor.utils.CommonUtil.delay;

import com.liam.projectreactor.models.MovieInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MovieInfoService {
	
	@Autowired
	private WebClient webClient;
	
//	private MovieInfoService(WebClient webClient) {
//		this.webClient = webClient;
//	}
	
	
	
//     _        _   _      
// ___| |_ __ _| |_(_) ___ 
/// __| __/ _` | __| |/ __|
//\__ \ || (_| | |_| | (__ 
//|___/\__\__,_|\__|_|\___|
//                        	
	
	
	// FLUX
	public Flux<MovieInfo> retrieveMoviesFlux(){

		List<MovieInfo> movieInfoList = List.of(
				new MovieInfo(100L, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(101L, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo(102L, "The Dark Knight Rises", 2008, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        return Flux.fromIterable(movieInfoList);
    }
	
	
	// MONO
    public Mono<MovieInfo> retrieveMovieInfoMonoUsingId(long movieId){

        MovieInfo movie = new MovieInfo(movieId, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        return Mono.just(movie);
    }
    
    

    public List<MovieInfo> movieList(){
        delay(1000);

        return List.of(
        		new MovieInfo(100L, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(101L, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo(102L, "The Dark Knight Rises", 2008, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
    }
    
    

    public MovieInfo retrieveMovieUsingId(long movieId){
        delay(1000);
        return new MovieInfo(movieId, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
    }

}
