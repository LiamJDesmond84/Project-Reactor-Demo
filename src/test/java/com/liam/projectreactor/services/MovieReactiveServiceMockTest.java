package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovieReactiveServiceMockTest {
	
	
	@Mock
	private MovieInfoService movieInfoService;
	
	@Mock
	private ReviewService reviewService;
	
	
	@InjectMocks
	MovieReactiveService movieReactiveService;
	
	
	@Test
	void GetAllMovies() {
		//given
		
		
		//when
		
		
		//then
	}

}
