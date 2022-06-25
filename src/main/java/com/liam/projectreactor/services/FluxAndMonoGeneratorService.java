package com.liam.projectreactor.services;

import java.util.List;

import reactor.core.publisher.Flux;

public class FluxAndMonoGeneratorService {
	
	
	public Flux<String> namesFlux() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")); // DB or a remote service call
		
	}
	
	public static void main(String[] args) {
		
		
	}

}
