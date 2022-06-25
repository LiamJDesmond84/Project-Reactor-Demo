package com.liam.projectreactor.services;

import java.util.List;

import reactor.core.publisher.Flux;

public class FluxAndMonoGeneratorService {
	

	
	public static void main(String[] args) {
		
		FluxAndMonoGeneratorService fluxAndMonoGenServ = new FluxAndMonoGeneratorService();
		
		fluxAndMonoGenServ.namesFlux()
			.subscribe(name -> {
				System.out.println("The name is: " + name);
			});
		
	}
	
	public Flux<String> namesFlux() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")); // DB or a remote service call
		
	}

}
