package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;

public class ColdAndHotPublisherTest {
	
	
	@Test
	void coldPublisherTest() {
		
		Flux<Integer> fluxRange = Flux.range(1, 10);
		
		
		fluxRange.subscribe(x -> System.out.println(x));
	}

}
