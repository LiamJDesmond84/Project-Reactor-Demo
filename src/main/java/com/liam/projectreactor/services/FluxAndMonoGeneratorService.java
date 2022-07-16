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
///    \  \/ /  _ \ /    \_/ ___\\__  \\   __\  
//\     \___(  <_> )   |  \  \___ / __ \|  |    ---- Static Method - FLUX
// \______  /\____/|___|  /\___  >____  /__|    ---- Sequential - 1 then the other
//        \/            \/     \/     \/     	
	
	public Flux<String> exploreConcat() {
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C"); // Normally would by coming from a remote service or DB
		
		Flux<String> defFlux = Flux.just("D", "E", "F");
		
		return Flux.concat(abcFlux, defFlux)
				.log();
	}
	

//_________                              __   __      __.__  __  .__     
//\_   ___ \  ____   ____   ____ _____ _/  |_/  \    /  \__|/  |_|  |__   ---- FLUX & MONO
///    \  \/ /  _ \ /    \_/ ___\\__  \\   __\   \/\/   /  \   __\  |  \  ------- Produces Flux as an output
//\     \___(  <_> )   |  \  \___ / __ \|  |  \        /|  ||  | |   Y  \ ---- Instance Method
// \______  /\____/|___|  /\___  >____  /__|   \__/\  / |__||__| |___|  / ---- Sequential - 1 then the other
//        \/            \/     \/     \/            \/                \/  
	
	
	public Flux<String> exploreConcatWith() {
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C"); // Normally would by coming from a remote service or DB
		
		Flux<String> defFlux = Flux.just("D", "E", "F"); // Normally would by coming from a remote service or DB
		
		return abcFlux.concatWith(defFlux)
				.log();
	}
	
	
	public Flux<String> exploreConcatWithMono() {
		
		
		Mono<String> aMono = Mono.just("A"); // Normally would by coming from a remote service or DB
		Mono<String> bMono = Mono.just("B");
		Mono<String> cMono = Mono.just("C");
		
		
		return aMono.concatWith(bMono)
					.concatWith(cMono)
					.log();
	}
	
	
	
//	   /\/\   ___ _ __ __ _  ___ 
//	  /    \ / _ \ '__/ _` |/ _ \  ---- FLUX Only
//	 / /\/\ \  __/ | | (_| |  __/  ---- Static Method
//	 \/    \/\___|_|  \__, |\___|  ---- Interweaved
//	                  |___/      	
	
	public Flux<String> exploreMerge() { // A, D, B, E, C, F
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C")
				.delayElements(Duration.ofMillis(100)); // Lower value comes out first
		
		Flux<String> defFlux = Flux.just("D", "E", "F")
				.delayElements(Duration.ofMillis(125));
		
		return Flux.merge(abcFlux, defFlux)
				.log();
	}
	
	
	

//                            __    __ _ _   _     
//  /\/\   ___ _ __ __ _  ___/ / /\ \ (_) |_| |__    
// /    \ / _ \ '__/ _` |/ _ \ \/  \/ / | __| '_ \   ------- Produces Flux as an output
/// /\/\ \  __/ | | (_| |  __/\  /\  /| | |_| | | |  ---- Instance Method - FLUX & MONO
//\/    \/\___|_|  \__, |\___| \/  \/ |_|\__|_| |_|  ---- Interweaved
//                 |___/                          


	public Flux<String> exploreMergeWith() { // A, D, B, E, C, F
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C")
				.delayElements(Duration.ofMillis(100)); // Lower value comes out first
		
		Flux<String> defFlux = Flux.just("D", "E", "F")
				.delayElements(Duration.ofMillis(125));
		
		
		return abcFlux.mergeWith(defFlux)
				.log();
	}
	
	
	
	public Flux<String> exploreMergeWithMono() { // A, B
		
		
		Mono<String> aMono = Mono.just("A"); // NO delay for Mono because it's just 1 element
		
		Mono<String> bMono = Mono.just("B"); // NO delay for Mono because it's just 1 element
		
		
		return aMono.mergeWith(bMono)
				.log();
	}
	
	
	

//                            __                             _   _       _ 
//  /\/\   ___ _ __ __ _  ___/ _\ ___  __ _ _   _  ___ _ __ | |_(_) __ _| |
// /    \ / _ \ '__/ _` |/ _ \ \ / _ \/ _` | | | |/ _ \ '_ \| __| |/ _` | |  
/// /\/\ \  __/ | | (_| |  __/\ \  __/ (_| | |_| |  __/ | | | |_| | (_| | |  ---- Static Method - FLUX
//\/    \/\___|_|  \__, |\___\__/\___|\__, |\__,_|\___|_| |_|\__|_|\__,_|_|  ---- Sequential - 1 then the other
//                 |___/                 |_|                               	
	
	public Flux<String> exploreMergeSequential() { // A, D, B, E, C, F
		
		
		Flux<String> abcFlux = Flux.just("A", "B", "C");
		
		Flux<String> defFlux = Flux.just("D", "E", "F");
		
		
		return Flux.mergeSequential(abcFlux, defFlux)
				.log();
	}
	
	
	

//	 ______       
//	/ _  (_)_ __    ---- Static Method - FLUX
//	\// /| | '_ \   ---- Waits for each Publisher to emit 1 element
//	 / //\ | |_) |  ---- Instance Method
//	/____/_| .__/   ---- Interweaved
//	       |_|      ---- Can be used to merge up-to 2 to 8 Publishers(Flux or Mono)
	       
	public Flux<String> exploreZip() { // 1ST Return Example:  "AD", "BE", "CF" - First element of every flux
									   // 2ND Return Example: "AD14", "BE25", "CF36" - First element of every flux
		
		Flux<String> abcFlux = Flux.just("A", "B", "C");
		
		Flux<String> defFlux = Flux.just("D", "E", "F");
		
		Flux<String> _123Flux = Flux.just("1", "2", "3");
		
		Flux<String> _456Flux = Flux.just("4", "5", "6");
		
//		return Flux.zip(abcFlux, defFlux, (w, x) -> w + x) // Tuple of n elements - Being combined.
//				.log();
		
		return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux) // Tuple of n elements
				.map(a -> a.getT1() + a.getT2() + a.getT3() + a.getT4()) // Combining Tuple elements
				.log();
	}      

//	 ______      __    __ _ _   _       
//	/ _  (_)_ __/ / /\ \ (_) |_| |__    ---- Instance Method - FLUX & MONO(If Mono must return a Mono)
//	\// /| | '_ \ \/  \/ / | __| '_ \   ---- Waits for each Publisher to emit 1 element
//	 / //\ | |_) \  /\  /| | |_| | | |  ---- Merges 2 Publishers into 1
//	/____/_| .__/ \/  \/ |_|\__|_| |_|  ---- Publishers are subscribed eagerly
//	       |_|                        	---- Continues until 1 Publisher sends an onComplete() event       
	       
	public Flux<String> exploreZipWith() { // "AD", "BE", "CF" - First element of every flux

			Flux<String> abcFlux = Flux.just("A", "B", "C");
			
			Flux<String> defFlux = Flux.just("D", "E", "F");
			

			
			return abcFlux.zipWith(defFlux, (x, y) -> x + y)
				.log();
			
			
	}
	
	
	public Mono<String> exploreZipWithMono() { // AB
		   

			Mono<String> aMono = Mono.just("A");
		
			Mono<String> bMono = Mono.just("B");
			

			
			return aMono.zipWith(bMono)
					.map(a -> a.getT1() + a.getT2()) // AB
					.log();
			
			
	}
	
	
	
	

//    ___        ___       
//   /   \___   /___\_ __  
//  / /\ / _ \ //  // '_ \ 
// / /_// (_) / \_//| | | |
///___,' \___/\___/ |_| |_|
//                         	
	
	
	public Flux<String> namesFluxMapFilterDoOnSomething(int stringLen) { // Filter the string whose length is greater than 3
		
		return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
				.map(String::toUpperCase)
				.filter(x -> x.length() > stringLen)
				.map(x -> x.length() + "-" + x) // Adding the name length + '-' to beginning of name
				.doOnNext(name -> {
					System.out.println("name is: " + name);
				})
				.doOnSubscribe(sub -> {
					System.out.println("Subscription is : " + sub);
					// Subscription is : reactor.core.publisher.FluxPeekFuseable$PeekFuseableSubscriber@14bdbc74
				})
				.doOnComplete(() -> {
					// Add whatever you want here.  Comes out RED sucka!
					System.err.println("Inside the doOnComplete callback");
				})
				.doFinally(signalType -> {
					System.out.println("Inside the doFinally callback - Signal Type: " + signalType);
				})
				// Gets invokes as a FINAL event in sequence, successfully or errored.  SIGNAL TYPE - Gives Last event that was emitted in the Reactive Stream
					// Inside the doFinally callback - Signal Type: onComplet
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
