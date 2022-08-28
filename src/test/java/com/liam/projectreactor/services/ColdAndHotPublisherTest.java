package com.liam.projectreactor.services;

import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class ColdAndHotPublisherTest {
	
	
	@Test
	void coldPublisherTest() {
		
		Flux<Integer> fluxRange = Flux.range(1, 10)
				.flatMap(x -> Mono.just(x)
					.subscribeOn(Schedulers.parallel())

				).log();
		
		Flux<Integer> fluxRange2 = Flux.range(1, 10)
				.flatMap(x -> Mono.just(x)
					.subscribeOn(Schedulers.parallel())

				).log();
		
		List<Integer> intRange = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		
		fluxRange.subscribe(x -> System.out.println("Subscriber 1: " + x));
		
		fluxRange2.subscribe(x -> System.out.println("Subscriber 2: " + x));
		
		Flux<Integer> fluxRange1 = Flux.fromIterable(intRange)
				.flatMap(x -> 
					Mono.just(x)
					.subscribeOn(Schedulers.parallel())

				)
				.map(x -> {
					log.info("Subscriber 3: " + x);
					return x;
				});
		
		fluxRange1.subscribe(x -> System.out.println("Subscriber 4: " + x));
				
	}

}
