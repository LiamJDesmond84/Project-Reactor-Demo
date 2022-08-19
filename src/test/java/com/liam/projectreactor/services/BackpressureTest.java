package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureTest {
	
	
	
	@Test
	void testBackPressure() {
		
		Flux<Integer> numberRange = Flux.range(1, 100);
		
		
		numberRange
			.subscribe(num -> {
				log.info("The number is: " + num);
			});
			
		
	}

}
