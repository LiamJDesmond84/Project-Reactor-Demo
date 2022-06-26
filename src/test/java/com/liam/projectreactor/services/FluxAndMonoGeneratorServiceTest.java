package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {
	
	FluxAndMonoGeneratorService fluxAndMonoGenServ = new FluxAndMonoGeneratorService();
	
	@Test
	void namesFlux() {
		
		//given
		
		
		//when
		Flux<String> namesFlux = fluxAndMonoGenServ.namesFlux();
		
		//then
		StepVerifier.create(namesFlux)
//			.expectNext("Alex", "Ben", "Chloe")
//			.expectNextCount(3)
			.expectNext("Alex") // First should be Alex
			.expectNextCount(2) // 2 left after Alex
			.verifyComplete();
	}
	
	@Test
	void nameMono() {
		
		//given
		
		
		//when
		Mono<String> nameMonoTest = fluxAndMonoGenServ.nameMono();
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("Alex")
			.expectNextCount(0)
			.verifyComplete();
	}
	
	
	@Test
	void namesFluxMap() {
		
		//given
		
		
		//when
		Flux<String> namesFluxMap = fluxAndMonoGenServ.namesFluxMap();
		
		//then
		StepVerifier.create(namesFluxMap)
			.expectNext("ALEX") // First should be ALEX
			.expectNext("BEN")
			.expectNextCount(1) // 1 left after BEN
			.verifyComplete();
	}
	
	@Test
	void namesFluxMapFilter() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> namesFluxMap = fluxAndMonoGenServ.namesFluxMapFilter(stringLen);
		
		//then
		StepVerifier.create(namesFluxMap)
			.expectNext("ALEX") // First should be ALEX
			.expectNext("CHLOE")
			.expectNextCount(0) // 0 left after CHLOE
			.verifyComplete();
	}
	
//	@Test
//	void namesFluxImm() { // Reactive Streams are immutable, only operators(like .map) attached to the datasource can transform the data
//		
//		//given
//		
//		
//		//when
//		Flux<String> namesFluxImm = fluxAndMonoGenServ.namesFluxImmutability();
//		
//		//then
//		StepVerifier.create(namesFluxImm)
//			.expectNext("ALEX") // First should be ALEX
//			.expectNext("BEN")
//			.expectNextCount(1) // 1 left after BEN
//			.verifyComplete();
//	}

}
