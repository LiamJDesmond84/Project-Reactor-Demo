package com.liam.projectreactor.services;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
				.log()
				// ALEX, CHLOE -> A, L, E, X, C, H, L, O, E - [A, L, E, X], [C, H, L, O, E] ???
				.flatMap(x -> splitString(x)); // BELOW METHOD
				// A, L, E, X, C, H, L, O, E
//				.log();
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
				// ALEX, CHLOE -> A, L, E, X, C, H, L, O, E - [A, L, E, X], [C, H, L, O, E] ???
				.concatMap(x -> splitStringWithDelay(x)) // BELOW METHOD
				// A, L, E, X, C, H, L, O, E
				.log();
	}
	

	
	
//	   ___ _       _                                               
//	   / __\ | __ _| |_  /\/\   __ _ _ __   /\/\   __ _ _ __  _   _ 
//	  / _\ | |/ _` | __|/    \ / _` | '_ \ /    \ / _` | '_ \| | | |
//	 / /   | | (_| | |_/ /\/\ \ (_| | |_) / /\/\ \ (_| | | | | |_| |   --  When returning a FLUX in your Mono pipeline
//	 \/    |_|\__,_|\__\/    \/\__,_| .__/\/    \/\__,_|_| |_|\__, |
//	                                |_|                       |___/ 
	
	//MONO - FLUX
	
	public Flux<String> nameMonoFlatMapMany(int stringLen) { // "Alex" -> "ALEX" -> splitString -> Mono<List> of [A, L, E, X]
		
		return Mono.just("Alex")
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.flatMapMany(this::splitString) // Mono<List> of [A, L, E, X]    -  BELOW METHOD
				.log();
	}
	

	// Flux - defaultIfEmpty
	public Flux<String> namesFluxTransform(int stringLen) { // This particular transform() method has the same exact output as the flatmap() method - But the operators are separated into a function, extracted and re-used in the transform()
		
				// Input type  // Result type
		Function<Flux<String>, Flux<String>> filterMap = inp -> inp // - Passing this function into .transform
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen);
		
		
		//Flux.empty()
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.transform(filterMap) // Using operators from filtermap ^^^ FUNCTION ^^^
				.flatMap(x -> splitString(x)) // BELOW METHOD
				// A, L, E, X, C, H, L, O, E
				.defaultIfEmpty("default String")
				.log();
	}
	
	// Mono - defaultIfEmpty
	public Mono<String> nameMonoTransform(int stringLen) { // This particular transform() method has the same exact output as the flatmap() method - But the operators are separated into a function, extracted and re-used in the transform()
		
				// Input type  // Result type
		Function<Mono<String>, Mono<String>> filterMap = inp -> inp // - Passing this function into .transform
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen);


		//Flux.empty()
		return Mono.just("Alex")
				.transform(filterMap) // Using operators from filtermap ^^^ FUNCTION ^^^
				// A, L, E, X, C, H, L, O, E
				.defaultIfEmpty("default String")
				.log();
}
	

//	   __                _         __                          
//	  /__\ __ ___  _ __ | |_ _   _/ _\ ___  _   _ _ __ ___ ___ 
//	 /_\| '_ ` _ \| '_ \| __| | | \ \ / _ \| | | | '__/ __/ _ \
//	//__| | | | | | |_) | |_| |_| |\ \ (_) | |_| | | | (_|  __/ - Along with .defaultIfEmpty() ABOVE ^^^
//	\__/|_| |_| |_| .__/ \__|\__, \__/\___/ \__,_|_|  \___\___|
//	              |_|        |___/     	

	public Flux<String> namesFluxTransformWithSwitchIfEmpty(int stringLen) { // This particular transform() method has the same exact output as the flatmap() method - But the operators are separated into a function, extracted and re-used in the transform()
		
				// Input type  // Result type
		Function<Flux<String>, Flux<String>> filterMap = inp -> inp // - Passing this function into .transform
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.flatMap(x -> splitString(x)); // BELOW METHOD
		
		Flux<String> defaultFlux = Flux.just("default String")
			.transform(filterMap); // "D", "E", "F", "A", "U", "L", "T", " ", "S", "T", "R", "I", "N", "G"
//			.log();
		
		//Flux.empty()
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.transform(filterMap) // Using operators from filtermap ^^^ FUNCTION ^^^
				.switchIfEmpty(defaultFlux) // using Flux with filtermap ^^^ FUNCTION ^^^
				.log();
	}
	
	public Mono<String> namesMonoTransformWithSwitchIfEmpty(int stringLen) { // This particular transform() method has the same exact output as the flatmap() method - But the operators are separated into a function, extracted and re-used in the transform()
		
				// Input type  // Result type
		Function<Mono<String>, Mono<String>> filterMap = inp -> inp // - Passing this function into .transform
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen);
		
		Mono<String> defaultFlux = Mono.just("default String")
			.transform(filterMap); // "DEFAULT STRING"
		//	.log();
		
		return Mono.just("Alex")
				.transform(filterMap) // Using operators from filtermap ^^^ FUNCTION ^^^
				.switchIfEmpty(defaultFlux) // using Mono with filtermap ^^^ FUNCTION ^^^
				.log();
}
	
// TRANSFORM INFO	
//	When you compose chains of operators regularly and you have common operator usage patterns in your application, you can mutualize this code or give it a more descriptive name by using transform and transformDeferred.
//
//	The difference between the two is when the mutualized operators are applied: transform applies them at instantiation, while transformDeferred applies them at subscription (allowing for dynamic choice of the added operators)
	

	

//======================___________.__                                 _____                        
//======================\_   _____/|  |  __ _____  ___     .__        /     \   ____   ____   ____  
//====================== |    __)  |  | |  |  \  \/  /   __|  |___   /  \ /  \ /  _ \ /    \ /  _ \ 
//====================== |     \   |  |_|  |  />    <   /__    __/  /    Y    (  <_> )   |  (  <_> )
//====================== \___  /   |____/____//__/\_ \     |__|     \____|__  /\____/|___|  /\____/ 
//======================     \/                     \/                      \/            \/        	
//======================	
	


//_________                              __   
//\_   ___ \  ____   ____   ____ _____ _/  |_ 
///    \  \/ /  _ \ /    \_/ ___\\__  \\   __\  ---- Only FLUX
//\     \___(  <_> )   |  \  \___ / __ \|  |  
// \______  /\____/|___|  /\___  >____  /__|  
//        \/            \/     \/     \/     	
	
	public Flux<String> exploreConcat() {
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C"); // Normally would by coming from a remote service or DB
		
		Flux<String> defFlux = Flux.just("D", "E", "F"); // Normally would by coming from a remote service or DB
		
		return Flux.concat(abcFlux, defFlux)
				.log();
	}
	

//_________                              __   __      __.__  __  .__     
//\_   ___ \  ____   ____   ____ _____ _/  |_/  \    /  \__|/  |_|  |__  
///    \  \/ /  _ \ /    \_/ ___\\__  \\   __\   \/\/   /  \   __\  |  \ 
//\     \___(  <_> )   |  \  \___ / __ \|  |  \        /|  ||  | |   Y  \ ---- FLUX & MONO
// \______  /\____/|___|  /\___  >____  /__|   \__/\  / |__||__| |___|  / ------- Produces Flux as an output
//        \/            \/     \/     \/            \/                \/ 
	
	
	public Flux<String> exploreConcatWith() {
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C"); // Normally would by coming from a remote service or DB
		
		Flux<String> defFlux = Flux.just("D", "E", "F"); // Normally would by coming from a remote service or DB
		
		return abcFlux.concatWith(defFlux)
				.log();
	}
	
	
	public Flux<String> exploreConcatWithMono() {
		
		
		Mono<String> abcFlux = Mono.just("A"); // Normally would by coming from a remote service or DB
		
		Mono<String> defFlux = Mono.just("D"); // Normally would by coming from a remote service or DB
		
		return abcFlux.concatWith(defFlux)
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
