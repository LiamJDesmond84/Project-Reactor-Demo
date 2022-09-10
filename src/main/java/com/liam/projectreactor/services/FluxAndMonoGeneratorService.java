package com.liam.projectreactor.services;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import static com.liam.projectreactor.utils.CommonUtil.delay;

import com.liam.projectreactor.exceptions.ReactorException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
@Slf4j
public class FluxAndMonoGeneratorService {
	

	
	public static void main(String[] args) {
		
		
		
//		Flux<String> debugTest = Flux.just("One", "Two", "Three").log();
//		
//		debugTest.subscribe(x -> log.debug("message " + x));
		
		FluxAndMonoGeneratorService fluxAndMonoGenServ = new FluxAndMonoGeneratorService();
		
//		fluxAndMonoGenServ.namesFlux()
//			.subscribe(name -> {
//				System.out.println("The name is: " + name);
//			});
//		
//
//		fluxAndMonoGenServ.nameMono()
//			.subscribe(name -> {
//				System.out.println("The Mono name is: " + name);
//			});
//		
//		fluxAndMonoGenServ.namesFluxMap()
//			.subscribe(x -> {
//				System.out.println("The name mapped to uppercase is: " + x);
//			});
//		
//		fluxAndMonoGenServ.namesFluxMapFilter(3)
//		.subscribe(x -> {
//			System.out.println("The filtered name mapped to uppercase is: " + x);
//		});
//		
//		fluxAndMonoGenServ.nameMonoFilter(3)
//		.subscribe(name -> {
//			System.out.println("The Mono String length + uppercase name is: " + name);
//		});
		
//		fluxAndMonoGenServ.namesFluxImmutability()
//		.subscribe(x -> {
//			System.out.println("The name mapped to uppercase is: " + x);
//		});
		

		fluxAndMonoGenServ.rangeErrors()
		.subscribe(x -> {
			System.out.println("Subscriber Test: " + x);
		});
		
	}
	
	
	// Flux.range() test
	public Flux<String> rangeErrors() {
		  return Flux.range(1, 30)
		      .map(i -> {
		        if (i == 30) {
		          throw new RuntimeException("Random error");
		        }
		        return i + ", foo bar";
		      }).log();
		}
	
	
	//MONO
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
				.log()
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
//  / /\ / _ \ //  // '_ \  --- Side effects
// / /_// (_) / \_//| | | | --- Do no effect the stream
///___,' \___/\___/ |_| |_| --- Allows you to react to certain stream events outside of the stream
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
	
	
//	___________                           __  .__                      
//	\_   _____/__  ___ ____  ____ _______/  |_|__| ____   ____   ______
//	 |    __)_\  \/  // ___\/ __ \\____ \   __\  |/  _ \ /    \ /  ___/
//	 |        \>    <\  \__\  ___/|  |_> >  | |  (  <_> )   |  \\___ \   --- Any exception will terminate the Reactive Stream??
//	/_______  /__/\_ \\___  >___  >   __/|__| |__|\____/|___|  /____  >  --- onErrorReturn("Something") - Stream continues after recovery
//	        \/      \/    \/    \/|__|                       \/     \/ 	 --- Review other ones
	
	
	public Flux<String> exceptionFlux() { // expectNext("A", "B", "C") - expectError()
		
		return Flux.just("A", "B", "C")
			.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
			.concatWith(Flux.just("D")) // Not going to be called because of Runtime Exception
			.log();
	}
	
	
	public Flux<String> exploreOnErrorReturn() { // "A", "B", "C", "D", "D"
		
		return Flux.just("A", "B", "C")
			.concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
			.onErrorReturn("D") // Recovers from error
			.concatWith(Flux.just("D")) // Stream will continue after recovery
			.log();
	}
	
	
	public Flux<String> exploreOnErrorResume(Exception e) { // "A", "B", "C", "D", "E", "F", "G"
		
		Flux<String> recoveryFlux = Flux.just("D", "E", "F");
		
		return Flux.just("A", "B", "C")
			.concatWith(Flux.error(e)) // Instead of hard-coding it, using exception from the parameter
			
			.onErrorResume(exc -> {  // Accepts type: Throwable, return type: Publisher(Flux in our case)
				log.error("The Exception is: " + exc);
				
				if(exc instanceof IllegalStateException) {
					return recoveryFlux; // return type: Publisher(Flux in our case)
				}
				
				else {
					return Flux.error(exc); // Because we used Flux.error as the "else", Stream terminates
				}							// Flux.error - Create a Flux that terminates with an error immediately
			})
			.concatWith(Flux.just("G")) // Stream will continue after recovery - The fallback Flux
			.log();
	}
	


//               __                     ___            _   _                  
//  ___  _ __   /__\ __ _ __ ___  _ __ / __\___  _ __ | |_(_)_ __  _   _  ___ 
// / _ \| '_ \ /_\| '__| '__/ _ \| '__/ /  / _ \| '_ \| __| | '_ \| | | |/ _ \  --- BiConsumer - Accepts: Exception, Value that caused the Exception
// |(_) | | | //__| |  | | | (_) | | / /__| (_) | | | | |_| | | | | |_| |  __/  --- Continues the stream - Just ignores the element that caused the Exception
// \___/|_| |_\__/|_|  |_|  \___/|_| \____/\___/|_| |_|\__|_|_| |_|\__,_|\___|
	
	
	public Flux<String> exploreOnErrorContinue() { // "A", "C", "G" - Skips B(which we made cause the error).
		
		
		return Flux.just("A", "B", "C")
			.map(x -> {
				if(x.equals("B")) {
					throw new IllegalStateException("Exception Occurred");
				}
				else {
					return x;
				}
			})
			
			.concatWith(Flux.just("D")) // Still logs D as well.  Already tested it below damnit.  This is redundant
			.onErrorContinue((exc, y) -> {
				log.error("The Exception is: " + exc);
				log.error("The Value is: " + y);
			})
			.concatWith(Flux.just("G")) // Stream will continue after continue
			.log();
	}
	
	public Mono<String> exploreOnErrorContinueMono(String inp) { // Either throws the exception & verifies complete or doesn't throw it & logs the input
		
		
		return Mono.just(inp)
			.map(x -> {
				if(x.equals("abc")) {
					throw new IllegalStateException("Exception Occurred");
				}
				else {
					return inp;
				}
			})
			.onErrorContinue((exc, y) -> {
				log.error("The Exception is: " + exc);
				log.error("The Value is: " + y);
			})
			.log();
	}
	


//	    		   __                                      
//	  ___  _ __   /__\ __ _ __ ___  _ __ /\/\   __ _ _ __  
//	 / _ \| '_ \ /_\| '__| '__/ _ \| '__/    \ / _` | '_ \  --- Does not recover from the exception
//	| (_) | | | //__| |  | | | (_) | | / /\/\ \ (_| | |_) | --- Transforms the exception from one type to another, like a BusinessException or Customer Exception
//	 \___/|_| |_\__/|_|  |_|  \___/|_| \/    \/\__,_| .__/  
//	                                                |_|   	
	
	public Flux<String> exploreOnErrorMap() { // "A" - "B" throws Exception - does not recover from error
		
		
		return Flux.just("A", "B", "C")
			.map(x -> {
				if(x.equals("B")) {
					throw new IllegalStateException("Exception Occurred");
				}
				else {
					return x;
				}
			})
			
			.concatWith(Flux.just("D")) // Does NOT log D - Does not recover from Exception
			.onErrorMap(exc -> { // Takes in a Function functional interface - Below return?
				log.error("The Exception is: " + exc);
				return new ReactorException(exc, exc.getMessage()); // We created this Custom? Exception - Takes in -> Throwable, Message
				
			})
			.concatWith(Flux.just("G"))  // Does NOT log G - Does not recover from Exception
			.log();
	}
	
	public Mono<String> exploreOnErrorMapMono() { // "A" - "B" throws Exception - does not recover from error
		
		
		return Mono.just("A")
			.map(x -> {
				if(x.equals("A")) {
					throw new IllegalStateException("Exception Occurred");
				}
				else {
					return x;
				}
			})
			.onErrorMap(exc -> { // Takes in a Function functional interface - Below return?
				log.error("The Exception is: " + exc);
				return new ReactorException(exc, exc.getMessage()); // We created this Custom? Exception - Takes in -> Throwable, Message
				
			})
			.log();
	}
	
//	public Mono<Object> exploreOnErrorMapMonoWithParam(Exception excep) { // "B" throws Exception - does not recover from error
//		
//		// WHAT THE HELL IS THIS
//		return Mono.just("B")
//			.map(x -> {
//				
//					throw new IllegalStateException("Exception Occurred");
//				
//				
//			})
//			
//			.onErrorMap(exc -> { // Takes in a Function functional interface - Below return?
//				log.error("The Exception is: " + exc);
//				return new ReactorException(exc, exc.getMessage()); // We created this Custom? Exception - Takes in -> Throwable, Message
//				
//			})
//			.log();
//	}
	
	
	

//	     _         ___         __                    
//	  __| | ___   /___\_ __   /__\ __ _ __ ___  _ __   --- Does NOT recovery from the Exception
//	 / _` |/ _ \ //  // '_ \ /_\| '__| '__/ _ \| '__|  --- Can take an action when an Exception occurs in the pipeline
//	| (_| | (_) / \_//| | | //__| |  | | | (_) | |     --- Does NOT modify the Reactve Stream
//	 \__,_|\___/\___/ |_| |_\__/|_|  |_|  \___/|_|     --- Falls under the doOn Callbacks category - Side Effect
//													   --- If you want to implement the traditional try/catch style of Exception handling - Can use doOnError
	
	
	public Flux<String> exploreDoOnError() { // "A", "B", "C"
		
		return Flux.just("A", "B", "C")
			.concatWith(Flux.error(new IllegalStateException("Exception Occurred")))
			.doOnError(exc -> { // Accepts a Consumer of type: Throwable(IllegalStateException in our case)
				log.error("The Exception is: " + exc);
			}) 
			.concatWith(Flux.just("D")) // Does NOT log D - Does not recover from Exception
			.log();
	}
	
	
	public Mono<Object> exploreOnErrorReturnMono() { // "abc"
		
		return Mono.just("A")
			.map(value -> {
				throw new RuntimeException("Exception Occurred");
			})
			.onErrorReturn("abc") 
			.log();
	}
	
	
	

//    							   _       
//  __ _  ___ _ __   ___ _ __ __ _| |_ ___ 
// / _` |/ _ \ '_ \ / _ \ '__/ _` | __/ _ \
//| (_| |  __/ | | |  __/ | | (_| | ||  __/ // SynchronousSink - Sequential - Maintains the state throughout the whole generation of events
// \__, |\___|_| |_|\___|_|  \__,_|\__\___|
// |___/                                  	
	
	
	// state & sink act seperately -> state = 1-10, sink = 2-20(evens)
	public Flux<Integer> explore_generate() {
		
							
		return Flux.generate(
				// Callable function with initial value(1) // bi-function // current value(state), sink.next, sink.complete, etc.
				() -> 1, (state, sink) -> {
					
					sink.next(state * 2);
					
					if(state == 10) {
						sink.complete(); // sink.complete() ends the loop
					}
					return state + 1; // mimics a for loop's behavior
		});
	}
	
	

//                     _       
//  ___ _ __ ___  __ _| |_ ___ 
// / __| '__/ _ \/ _` | __/ _ \ // Asynchronous & multi-threaded
//| (__| | |  __/ (_| | ||  __/ // Generate & emit events from multiple threads
// \___|_|  \___|\__,_|\__\___| // FluxSink - onNext, onComplete, onError
								// create - good for introducing Reactive nature into our Asynchronous code - Multiple threads involved in emitting the events
	
// All 3 methods mimic ASYNCHRONOUS & MULTI-THREADING behavior with our Reactive Flow
	
	public Flux<String> explore_create() {
		
		return Flux.create(sink -> { // Flux.create - Use an existing function & bring it in to the Reactive world.
			
//			names().forEach(name -> {
//				sink.next(name);
//			});
//			names().forEach(sink::next); // shorter version
			CompletableFuture
				.supplyAsync(() -> names())
				
//				.thenAccept(x -> {
//					x.forEach(sink::next);
//				})
				
				.thenAccept(x -> { // Sending same event multiple times
					x.forEach((y) -> {
						sink.next(y);
						sink.next(y);
					});
				})
				
//				.thenRun(() -> sink.complete()); // once the operators above are completed then run sink.complete.  (Can be sink::complete instead)
				.thenRun(() -> sendEvents(sink));

		});
	}
	
	
	public Mono<String> explore_create_mono() {
		
		return Mono.create(sink -> {
			
			sink.success("alex"); // value can come from another service or remote DB call as well(CompletableFuture), but for now we're making it static
			
		});
		
	}
	
	
	
	public void sendEvents(FluxSink<String> sink) {
		

			CompletableFuture
				.supplyAsync(() -> names())
				.thenAccept(x -> {
					x.forEach(sink::next);
				})
				.thenRun(sink::complete); // once the operators above are completed then run sink.complete.  (Cannot do sink.complete() here for some reason)
//			sink.complete();
		
	}
	
	public Flux<String> explore_handle() { // basically a simple map filter below
		
		return Flux.fromIterable(List.of("alex", "ben", "chloe"))
				.handle((name, sink) -> {
					if(name.length() > 3) {
						sink.next(name.toUpperCase());
					}
				});
		
	}
	
	
	
	
	public static List<String> names() {
		
		delay(1000);
		return List.of("alex", "ben", "chloe");
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
