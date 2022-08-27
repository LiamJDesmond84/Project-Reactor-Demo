package com.liam.projectreactor.services;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import static com.liam.projectreactor.utils.CommonUtil.delay;

@Slf4j
public class FluxAndMonoSchedulersService {
	
	static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");
    
    

//    		   _     _ _     _       ___       
// _ __  _   _| |__ | (_)___| |__   /___\_ __  
//| '_ \| | | | '_ \| | / __| '_ \ //  // '_ \ 
//| |_) | |_| | |_) | | \__ \ | | / \_//| | | |
//| .__/ \__,_|_.__/|_|_|___/_| |_\___/ |_| |_|
//|_|                                              
    
    public Flux<String> explorePublishOn() {
    	
    	Flux<String> namesFlux =  Flux.fromIterable(namesList)
    			.publishOn(Schedulers.parallel())
//    			.publishOn(Schedulers.boundedElastic()) // Just to show seperate threads
    			.map(this::upperCase) // with delay method below
    			.map(x -> {
    				log.info("Thread 1: {}", x);
    				return x;
    			})
//    			.map(x -> x.toUpperCase())
    			.log();
    	
    	
    	
    	
    	Flux<String> namesFlux1 =  Flux.fromIterable(namesList1)
    			.publishOn(Schedulers.parallel())
    			.map(this::upperCase) // with delay method below
    			.map(x -> {
    				log.info("Thread 2: {}", x);
    				return x;
    			})
    			.log();
    	
    	return namesFlux.mergeWith(namesFlux1);
    }
    
    
//    		 _                   _ _            ___       
// ___ _   _| |__  ___  ___ _ __(_) |__   ___  /___\_ __  
/// __| | | | '_ \/ __|/ __| '__| | '_ \ / _ \//  // '_ \ 
//\__ \ |_| | |_) \__ \ (__| |  | | |_) |  __/ \_//| | | | - Similar to publishOn but influences the entire stream
//|___/\__,_|_.__/|___/\___|_|  |_|_.__/ \___\___/ |_| |_|
//                                                     
    
    public Flux<String> exploreSubscribeOn() {
    	
    	Flux<String> namesFlux =  flux1(namesList)
    			.subscribeOn(Schedulers.boundedElastic())
    			.map(x -> {
    				log.info("Thread 1: {}", x);
    				return x;
    			})
    			.log();
    	
    	
    	
    	Flux<String> namesFlux1 =  flux1(namesList1)
    			.subscribeOn(Schedulers.boundedElastic())
    			.map(x -> {
    				log.info("Thread 2: {}", x);
    				return x;
    			})
    			.log();
    	
    	return namesFlux.mergeWith(namesFlux1);
    }


	private Flux<String> flux1(List<String> namesListParam) {
		return Flux.fromIterable(namesListParam)
				.map(this::upperCase) // with delay method below(uppercase)
	;
	}

    

//    					 _ _      _   ___ _            
// _ __   __ _ _ __ __ _| | | ___| | / __\ |_   ___  __
//| '_ \ / _` | '__/ _` | | |/ _ \ |/ _\ | | | | \ \/ /
//| |_) | (_| | | | (_| | | |  __/ / /   | | |_| |>  <   -- Disadvantage - different types of operations that can be performed are very limited
//| .__/ \__,_|_|  \__,_|_|_|\___|_\/    |_|\__,_/_/\_\                     - i.e. .parallel() flux won't allow other specific operators(like merge())
//|_|             
	
// .parallel() & .flatMap with Schedulers.parallel() are 2 different ways of achieving the same thing
    
    public ParallelFlux<String> explore_parallel() { // showing sequential behavior of the reactive pipeline
    	
    	Integer cores = Runtime.getRuntime().availableProcessors(); // Showing # of cores of your machine - More cores == more processing more things at the same time == faster
    	log.info("Number of cores: " + cores);
    	
    	return Flux.fromIterable(namesList)
//			.publishOn(Schedulers.parallel())

    		.doOnNext(x -> {
        		log.info("Before parallel: " + x);
        		})
    		.parallel()
    		.runOn(Schedulers.parallel())
    		.doOnNext(x -> {
    			log.info("Parallel starting now: " + x);
    		})
			.map(this::upperCase) // with delay method below
			.map(x -> {
				log.info("Thread 1: {}", x);
				return x;
			})
			.log();
    	
    }
    
    
	public Flux<String> explore_parallel_using_flatMap() { // showing sequential behavior of the reactive pipeline
	    	
	    	
	    	return Flux.fromIterable(namesList)
	//    		.flatMap(name -> { // flatMap returns a reactive type
	//    			return Mono.just(name)
	//    				.map(this::upperCase) // invoking a blocking call
	//    				.subscribeOn(Schedulers.parallel());
	//    		})
	    		.flatMap(name -> Mono.just(name)
	    				.map(this::upperCase) // invoking a blocking call
	    				.subscribeOn(Schedulers.parallel()))
				.log();
	    	
	}

	public Flux<String> explore_parallel_using_flatMap_1() { // Using .flatMap with Schedulers.parallel() = .parallal()
		
		Flux<String> namesFlux =  Flux.fromIterable(namesList)
				.flatMap(name -> Mono.just(name)
	    				.map(this::upperCase) // invoking a blocking call
	    				.subscribeOn(Schedulers.parallel()))
				.log();
		
		Flux<String> namesFlux1 =  Flux.fromIterable(namesList1)
				.flatMap(name -> Mono.just(name)
	    				.map(this::upperCase) // invoking a blocking call
	    				.subscribeOn(Schedulers.parallel()))
				.map(x -> {
					log.info("Thread 2: {}", x);
					return x;
				})
				.log();
		
		return namesFlux.mergeWith(namesFlux1);
	}
	
	public Flux<String> explore_parallel_using_flatMapSequential() { // showing sequential behavior of the reactive pipeline
    	
    	
    	return Flux.fromIterable(namesList)
//    		.flatMap(name -> { // flatMap returns a reactive type
//    			return Mono.just(name)
//    				.map(this::upperCase) // invoking a blocking call
//    				.subscribeOn(Schedulers.parallel());
//    		})
    		.flatMap(name -> Mono.just(name)
    				.map(this::upperCase) // invoking a blocking call
    				.subscribeOn(Schedulers.parallel()))
			.log();
    	
    }
    
    
    private String upperCase(String name) {
        delay(1000); // mocking a "blocking" call
        return name.toUpperCase();
    }

}
