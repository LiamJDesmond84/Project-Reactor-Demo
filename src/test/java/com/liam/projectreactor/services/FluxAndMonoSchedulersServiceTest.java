package com.liam.projectreactor.services;



import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;
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
	
	@Test
	void explore_parallel() {
		
		//given
		
		
		//when
		ParallelFlux<String> namesFlux = fluxAndMonoSchedulersService.explore_parallel();
		
		//then
		StepVerifier.create(namesFlux)
//			.expectNext("Alex", "Ben", "Chloe")
			.expectNextCount(3)
//			.expectNext("Alex") // First should be Alex
//			.expectNextCount(2) // 2 left after Alex
			.verifyComplete();
	}
	
	
	@Test
	void explore_parallel_using_flatMap() {
		
		//given
		
		
		//when
		Flux<String> namesFlux = fluxAndMonoSchedulersService.explore_parallel_using_flatMap();
		
		//then
		StepVerifier.create(namesFlux)
//			.expectNext("Alex", "Ben", "Chloe") // Random & capitalized, not reliable
			.expectNextCount(3)
			.verifyComplete();
	}
	
	
	@Test
	void explore_parallel_using_flatMap_1() { // 2 Flux.fromIterables - Total of 6
		
		//given
		
		
		//when
		Flux<String> namesFlux = fluxAndMonoSchedulersService.explore_parallel_using_flatMap_1();
		
		//then
		StepVerifier.create(namesFlux)
			.expectNextCount(6)
			.verifyComplete();
	}
	
	@Test
	void explore_parallel_using_flatMapSequential() { // 2 Flux.fromIterables - Total of 6
		
		//given
		
		
		//when
		Flux<String> namesFlux = fluxAndMonoSchedulersService.explore_parallel_using_flatMapSequential();
		
		//then
		StepVerifier.create(namesFlux)
		.expectNext("ALEX", "BEN", "CHLOE")
			.expectNextCount(0)
			.verifyComplete();
	}

}
