package com.liam.projectreactor.services;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {
	

	
	public static void main(String[] args) {
		
		FluxAndMonoGeneratorService fluxAndMonoGenServ = new FluxAndMonoGeneratorService();
		
		fluxAndMonoGenServ.namesFlux()
			.subscribe(name -> {
				System.out.println("The name is: " + name);
			});
		

		fluxAndMonoGenServ.nameMono()
			.subscribe(name -> {
				System.out.println("The Mono name is: " + name);
			});
		
	}
	
	public Mono<String> nameMono() {
		
		return Mono.just("Alex")
				.log();
	}
	
	public Flux<String> namesFlux() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.log(); // DB or a remote service call
		
	}

}
