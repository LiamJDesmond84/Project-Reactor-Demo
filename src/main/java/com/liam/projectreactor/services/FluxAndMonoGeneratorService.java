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
		
		fluxAndMonoGenServ.namesFluxMap()
			.subscribe(x -> {
				System.out.println("The name mapped to uppercase is: " + x);
			});
		
		fluxAndMonoGenServ.namesFluxMapFilter(3)
		.subscribe(x -> {
			System.out.println("The filtered name mapped to uppercase is: " + x);
		});
		
//		fluxAndMonoGenServ.namesFluxImmutability()
//		.subscribe(x -> {
//			System.out.println("The name mapped to uppercase is: " + x);
//		});
		
	}
	
	public Mono<String> nameMono() {
		
		return Mono.just("Alex")
				.log();
	}
	
	public Flux<String> namesFlux() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")) // DB or a remote service call
				.log(); 
		
	}
	
	public Flux<String> namesFluxMap() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
//				.map(s -> s.toUpperCase()) // Lambda
				.log();
	}
	
	
	public Flux<String> namesFluxMapFilter(int stringLen) { // Filter the string whose length is greater than 3
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.map(x -> x.length() + "-" + x)
				.log();
	}
	
	
	
//	public Flux<String> namesFluxImmutability() {
//		
//		Flux<String> namesFluxImm = Flux.fromIterable(List.of("Alex", "Ben", "Chloe"));
//
//		namesFluxImm.map(String::toUpperCase); // DOES NOT CHANGE ORIGINAL SOURCE(namesFluxImm)
//		
//		return namesFluxImm; // DOES NOT CHANGE ORIGINAL SOURCE(namesFluxImm)
//	}

}