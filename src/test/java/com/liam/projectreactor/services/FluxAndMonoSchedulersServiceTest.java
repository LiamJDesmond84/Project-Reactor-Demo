package com.liam.projectreactor.services;



import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoSchedulersServiceTest {
	
	FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

	@Test
	void explorePublishOn() {
		
		//given
		
		
		//when
		Flux<String> flux = fluxAndMonoSchedulersService.explorePublishOn();
		
		//then
		
		StepVerifier.create(flux)
			.expectNextCount(6)
			.verifyComplete();
	}
	
	
	@Test
	void exploreSubscribeOn() {
		
		//given
		
		
		//when
		Flux<String> flux = fluxAndMonoSchedulersService.exploreSubscribeOn();
		
		//then
		
		StepVerifier.create(flux)
			.expectNextCount(6)
			.verifyComplete();
	}

}