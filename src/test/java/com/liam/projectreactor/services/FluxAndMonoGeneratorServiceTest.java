package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
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
			.expectNext("Alex", "Ben", "Chloe")
			.verifyComplete();
	}

}
