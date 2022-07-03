package com.liam.projectreactor.services;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
		
		fluxAndMonoGenServ.nameMonoFilter(3)
		.subscribe(name -> {
			System.out.println("The Mono String length + uppercase name is: " + name);
		});
		
//		fluxAndMonoGenServ.namesFluxImmutability()
//		.subscribe(x -> {
//			System.out.println("The name mapped to uppercase is: " + x);
//		});
		
		
		
	//MONO
		
	}
	
	public Mono<String> nameMono() {
		
		return Mono.just("Alex")
				.log();
	}
	
	//FLUX
	
	public Flux<String> namesFlux() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")) // DB or a remote service call
				.log(); 
		
	}
	
	//FLUX
//	   /\/\   __ _ _ __  
//	  /    \ / _` | '_ \ 
//	 / /\/\ \ (_| | |_) |
//	 \/    \/\__,_| .__/ 
//	              |_| 
	
	public Flux<String> namesFluxMap() {
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
//				.map(s -> s.toUpperCase()) // Lambda
				.log();
	}
	
	//FLUX

//                      ___ _ _ _            
//  /\/\   __ _ _ __   / __(_) | |_ ___ _ __ 
// /    \ / _` | '_ \ / _\ | | | __/ _ \ '__|
/// /\/\ \ (_| | |_) / /   | | | ||  __/ |   
//\/    \/\__,_| .__/\/    |_|_|\__\___|_|   
//             |_|                           
//	
	public Flux<String> namesFluxMapFilter(int stringLen) { // Filter the string whose length is greater than 3
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.map(x -> x.length() + "-" + x) // Adding the name length + '-' to beginning of name
				.log();
	}
	
	//MONO
	
	public Mono<String> nameMonoFilter(int stringLen) {
		
		return Mono.just("Alex")
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.map(x -> x.length() + "-" + x); // Adding the name length + '-' to beginning of name
//				.log();
	}
	
	
	//FLUX

//	   ___ _       _                      
//	  / __\ | __ _| |_  /\/\   __ _ _ __  
//	 / _\ | |/ _` | __|/    \ / _` | '_ \ 
//	/ /   | | (_| | |_/ /\/\ \ (_| | |_) |
//	\/    |_|\__,_|\__\/    \/\__,_| .__/ 
//	                               |_|  
	
	// 1: "Alex", "Ben", "Chloe" - Ben goes away
	// 2: splitString - [A, L, E, X] - [C, H, L, O, E]
	// 3: flatmap - A, L, E, X, C, H, L, O, E
	public Flux<String> namesFluxFlatmap(int stringLen) { // Filter the string whose length is greater than 3
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				// ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
				.flatMap(x -> splitString(x)) // BELOW METHOD
				// A, L, E, X, C, H, L, O, E
				.log();
	}
	

	
	//MONO
	
	public Mono<List<String>> nameMonoFlatMap(int stringLen) { // "Alex" -> "ALEX" -> splitString -> Mono<List> of [A, L, E, X]
		
		return Mono.just("Alex")
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.flatMap(this::splitStringMono) // Mono<List> of A, L, E, X    -  BELOW METHOD
				.log();
	}
	

 	
	
	//FLUX
	//FLATMAP - Good with Async - But beware of DELAYED ELEMENTS - random? CONCATMAP might be better

//	   ___ _       _                        _                        
//	  / __\ | __ _| |_  /\/\   __ _ _ __   /_\  ___ _   _ _ __   ___ 
//	 / _\ | |/ _` | __|/    \ / _` | '_ \ //_\\/ __| | | | '_ \ / __|
//	/ /   | | (_| | |_/ /\/\ \ (_| | |_) /  _  \__ \ |_| | | | | (__ 
//	\/    |_|\__,_|\__\/    \/\__,_| .__/\_/ \_/___/\__, |_| |_|\___|
//	                               |_|              |___/            
	public Flux<String> namesFluxFlatmapAsync(int stringLen) { // Filter the string whose length is greater than 3
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				// ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
				.flatMap(x -> splitStringWithDelay(x)) // BELOW METHOD
				// A, L, E, X, C, H, L, O, E
				.log();
	}
	
//	   ___                      _                      
//	  / __\___  _ __   ___ __ _| |_  /\/\   __ _ _ __  
//	 / /  / _ \| '_ \ / __/ _` | __|/    \ / _` | '_ \ 
//	/ /__| (_) | | | | (_| (_| | |_/ /\/\ \ (_| | |_) |
//	\____/\___/|_| |_|\___\__,_|\__\/    \/\__,_| .__/ 
//	                                            |_|   
	
// Like flatMap, but preserves order
	
	public Flux<String> namesFluxContcatMap(int stringLen) { // Filter the string whose length is greater than 3
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				// ALEX, CHLOE -> A, L, E, X, C, H, L, O, E
				.concatMap(x -> splitStringWithDelay(x)) // BELOW METHOD
				// A, L, E, X, C, H, L, O, E
				.log();
	}
	

	
	
//	   ___ _       _                                               
//	   / __\ | __ _| |_  /\/\   __ _ _ __   /\/\   __ _ _ __  _   _ 
//	  / _\ | |/ _` | __|/    \ / _` | '_ \ /    \ / _` | '_ \| | | |
//	 / /   | | (_| | |_/ /\/\ \ (_| | |_) / /\/\ \ (_| | | | | |_| |   --  Mono that returns a FLUX
//	 \/    |_|\__,_|\__\/    \/\__,_| .__/\/    \/\__,_|_| |_|\__, |
//	                                |_|                       |___/ 
	
	//MONO - FLUX
	
	public Flux<String> nameMonoFlatMapMany(int stringLen) { // "Alex" -> "ALEX" -> splitString -> Mono<List> of [A, L, E, X]
		
		return Mono.just("Alex")
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.flatMapMany(this::splitString) // Mono<List> of A, L, E, X    -  BELOW METHOD
				.log();
	}
	


//	  _____                 _ _     _   _____          _           
//	  \_   \_ ____   ____ _| (_) __| | /__   \___  ___| |_         
//	   / /\/ '_ \ \ / / _` | | |/ _` |   / /\/ _ \/ __| __|  _____ 
//	/\/ /_ | | | \ V / (_| | | | (_| |  / / |  __/\__ \ |_  |_____|
//	\____/ |_| |_|\_/ \__,_|_|_|\__,_|  \/   \___||___/\__|        
//	                                                               
//	  _____                           _        _     _      
//	  \_   \_ __ ___  _ __ ___  _   _| |_ __ _| |__ | | ___ 
//	   / /\/ '_ ` _ \| '_ ` _ \| | | | __/ _` | '_ \| |/ _ \
//	/\/ /_ | | | | | | | | | | | |_| | || (_| | |_) | |  __/  - Cannot return directly - Something like that
//	\____/ |_| |_| |_|_| |_| |_|\__,_|\__\__,_|_.__/|_|\___|
//	                                                        

	
	
//	public Flux<String> namesFluxImmutability() {
//		
//		Flux<String> namesFluxImm = Flux.fromIterable(List.of("Alex", "Ben", "Chloe"));
//
//		namesFluxImm.map(String::toUpperCase); // DOES NOT CHANGE ORIGINAL SOURCE(namesFluxImm)
//		
//		return namesFluxImm; // DOES NOT CHANGE ORIGINAL SOURCE(namesFluxImm)
//	}
	
	
	
	

//	 __       _ _ _   __ _        _             
//	/ _\_ __ | (_) |_/ _\ |_ _ __(_)_ __   __ _ 
//	\ \| '_ \| | | __\ \| __| '__| | '_ \ / _` |
//	_\ \ |_) | | | |__\ \ |_| |  | | | | | (_| |
//	\__/ .__/|_|_|\__\__/\__|_|  |_|_| |_|\__, |
//	   |_|                                |___/ 
	
	// ALEX -> A, L, E, X
	public Flux<String> splitString(String name) {
		
		String[] charArray = name.split("");
		System.out.println(Arrays.toString(charArray));
		// [A, L, E, X]
		// [C, H, L, O, E]
		return Flux.fromArray(charArray);
//				.log();
	}
	
	
	public Flux<String> splitStringWithDelay(String name) {
		
		String[] charArray = name.split("");
		
		int delay = new Random().nextInt(1000);
		
		System.out.println(Arrays.toString(charArray));
		// [A, L, E, X]
		// [C, H, L, O, E]
		return Flux.fromArray(charArray)
				// delays each element that is being emitted - ends up random?
				.delayElements(Duration.ofMillis(1000)); // Or int delay
//				.log();
	}
	
	
	public Mono<List<String>> splitStringMono(String str) {
		String[] charArr = str.split("");
		
		List<String> charList = List.of(charArr); // ALEX -> A, L, E, X
		
		return Mono.just(charList);
	}

}
