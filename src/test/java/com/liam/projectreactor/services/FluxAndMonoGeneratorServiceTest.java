package com.liam.projectreactor.services;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.liam.projectreactor.exceptions.ReactorException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

public class FluxAndMonoGeneratorServiceTest {
	
	FluxAndMonoGeneratorService fluxAndMonoGenServ = new FluxAndMonoGeneratorService();
	
	@Test
	void namesFlux() {
		
		//given
		
		
		//when
		Flux<String> namesFlux = fluxAndMonoGenServ.namesFlux();
		
		//then
		StepVerifier.create(namesFlux)
//			.expectNext("Alex", "Ben", "Chloe")
//			.expectNextCount(3)
			.expectNext("Alex") // First should be Alex
			.expectNextCount(2) // 2 left after Alex
			.verifyComplete();
	}
	
	@Test
	void nameMono() {
		
		//given
		
		
		//when
		Mono<String> nameMonoTest = fluxAndMonoGenServ.nameMono();
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("Alex")
			.expectNextCount(0)
			.verifyComplete();
	}
	
	
	@Test
	void namesFluxMap() {
		
		//given
		
		
		//when
		Flux<String> namesFluxMap = fluxAndMonoGenServ.namesFluxMap();
		
		//then
		StepVerifier.create(namesFluxMap)
			.expectNext("ALEX") // First should be ALEX
			.expectNext("BEN")
			.expectNextCount(1) // 1 left after BEN
			.verifyComplete();
	}
	
	@Test
	void namesFluxMapFilter() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> namesFluxMap = fluxAndMonoGenServ.namesFluxMapFilter(stringLen);
		
		//then
		StepVerifier.create(namesFluxMap)
			.expectNext("4-ALEX", "5-CHLOE")
			.verifyComplete();
	}
	
	
	@Test
	void nameMonoFilter() {
		
		//given
		int stringLen = 3;
		
		//when
		Mono<String> nameMonoTest = fluxAndMonoGenServ.nameMonoFilter(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("4-ALEX")
			.expectNextCount(0)
			.verifyComplete();
	}
	
	@Test
	void namesFluxFlatmap() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.namesFluxFlatmap(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("A","L", "E", "X", "C", "H", "L", "O", "E")
			.expectNextCount(0)
			.verifyComplete();
	}
	
	@Test
	void namesFluxFlatmapAsync() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.namesFluxFlatmapAsync(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
//			.expectNext("A","L", "E", "X", "C", "H", "L", "O", "E")
			.expectNextCount(9)
			// Without an operator like expectNextCount, it won't veryify complete, as it's still waiting for elements to come through
			.verifyComplete();
	}
	
	
	@Test
	void namesFluxContcatMap() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.namesFluxContcatMap(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("A","L", "E", "X", "C", "H", "L", "O", "E")
//			.expectNextCount(9)
			.verifyComplete();
	}
	
	@Test
	void nameMonoFlatMap() {
		
		//given
		int stringLen = 3;
		
		//when
		Mono<List<String>> nameMonoTest = fluxAndMonoGenServ.nameMonoFlatMap(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext(List.of("A","L", "E", "X"))
//			.expectNextCount(9)
			.verifyComplete();
	}
	
	
	@Test
	void nameMonoFlatMapMany() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.nameMonoFlatMapMany(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("A","L", "E", "X")
//			.expectNextCount(9)
			.verifyComplete();
	}
	
	@Test
	void namesFluxTransform() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.namesFluxTransform(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("A","L", "E", "X", "C", "H", "L", "O", "E")
			.expectNextCount(0)
			.verifyComplete();
	}
	
	@Test
	void namesFluxTransform_1() { // Empty source after filter - stringLen = 6
		
		//given
		int stringLen = 6;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.namesFluxTransform(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
//			.expectNext("A","L", "E", "X", "C", "H", "L", "O", "E")
			.expectNext("default String")
			.verifyComplete();
	}
	
	@Test
	void namesFluxTransform_With_SwitchIfEmpty() { // Empty source after filter - stringLen = 6
		
		//given
		int stringLen = 6;
		
		//when
		Flux<String> nameMonoTest = fluxAndMonoGenServ.namesFluxTransformWithSwitchIfEmpty(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
//			.expectNext("D", "E", "F", "A", "U", "L", "T", " ", "S", "T", "R", "I", "N", "G")
			.expectNextCount(14)
			.verifyComplete();
	}
	
	@Test
	void nameMonoTransform() { // Empty source after filter - stringLen = 4
		
		//given
		int stringLen = 4;
		
		//when
		Mono<String> nameMonoTest = fluxAndMonoGenServ.nameMonoTransform(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("default String")
			.verifyComplete();
	}
	
	@Test
	void namesMonoTransformWithSwitchIfEmpty() { // Empty source after filter - stringLen = 4
		
		//given
		int stringLen = 4;
		
		//when
		Mono<String> nameMonoTest = fluxAndMonoGenServ.namesMonoTransformWithSwitchIfEmpty(stringLen);
		
		//then
		StepVerifier.create(nameMonoTest)
			.expectNext("DEFAULT STRING")
			.verifyComplete();
	}
	
	//======================___________.__                                 _____                        
	//======================\_   _____/|  |  __ _____  ___     .__        /     \   ____   ____   ____  
	//====================== |    __)  |  | |  |  \  \/  /   __|  |___   /  \ /  \ /  _ \ /    \ /  _ \ 
	//====================== |     \   |  |_|  |  />    <   /__    __/  /    Y    (  <_> )   |  (  <_> )
	//====================== \___  /   |____/____//__/\_ \     |__|     \____|__  /\____/|___|  /\____/ 
	//======================     \/                     \/                      \/            \/        	
	//======================	
	
	@Test
	void exploreConact() {
		
		//given

		
		//when
		Flux<String> concatTest = fluxAndMonoGenServ.exploreConcat();
		
		//then
		StepVerifier.create(concatTest)
			.expectNext("A", "B", "C", "D", "E", "F")
			.verifyComplete();
	}
	
	
	@Test
	void exploreConcatWith() {
		
		//given

		
		//when
		Flux<String> concatTest = fluxAndMonoGenServ.exploreConcatWith();
		
		//then
		StepVerifier.create(concatTest)
			.expectNext("A", "B", "C", "D", "E", "F")
			.verifyComplete();
	}
	
	@Test
	void exploreConcatWithMono() {
		
		//given

		
		//when
		Flux<String> concatTest = fluxAndMonoGenServ.exploreConcatWithMono();
		
		//then
		StepVerifier.create(concatTest)
			.expectNext("A", "B", "C")
			.verifyComplete();
	}
	
	@Test
	void exploreMerge() {
		
		//given

		
		//when
		Flux<String> value = fluxAndMonoGenServ.exploreMerge();
		
		//then
		StepVerifier.create(value)
			.expectNext("A", "D", "B", "E", "C", "F")
			.verifyComplete();
	}
	
	
	@Test
	void exploreMergeWith() {
		
		//given

		
		//when
		Flux<String> value = fluxAndMonoGenServ.exploreMerge();
		
		//then
		StepVerifier.create(value)
			.expectNext("A", "D", "B", "E", "C", "F")
			.verifyComplete();
	}
	
	@Test
	void exploreMergeWithMono() {
		
		//given

		
		//when
		Flux<String> value = fluxAndMonoGenServ.exploreMergeWithMono();
		
		//then
		StepVerifier.create(value)
			.expectNext("A", "B")
			.verifyComplete();
	}
	
	
	@Test
	void exploreMergeSequential() {
		
		//given

		
		//when
		Flux<String> value = fluxAndMonoGenServ.exploreMergeSequential();
		
		//then
		StepVerifier.create(value)
			.expectNext("A", "B", "C", "D", "E", "F")
			.verifyComplete();
	}
	
	
	@Test
	void exploreZip() {
		
		//given

		
		//when
		Flux<String> value = fluxAndMonoGenServ.exploreZip();
		
		//then
		StepVerifier.create(value)
//			.expectNext("AD", "BE", "CF")
			.expectNext("AD14", "BE25", "CF36")
			.verifyComplete();
	}
	
	
	@Test
	void exploreZipWith() {
		
		//given

		
		//when
		Flux<String> value = fluxAndMonoGenServ.exploreZipWith();
		
		//then
		StepVerifier.create(value)
			.expectNext("AD", "BE", "CF")
			.verifyComplete();
	}
	
	
	@Test
	void exploreZipWithMono() {
		
		//given

		
		//when
		Mono<String> value = fluxAndMonoGenServ.exploreZipWithMono();
		
		//then
		StepVerifier.create(value)
			.expectNext("AB")
			.verifyComplete();
	}
	

//     ___        ___       
//    /   \___   /___\_ __  
//   / /\ / _ \ //  // '_ \ 
//  / /_// (_) / \_//| | | | --- Operator set in the actual method
// /___,' \___/\___/ |_| |_|
//                       	
		
	
	
	@Test
	void namesFluxMapFilterDoOnSomething() {
		
		//given
		int stringLen = 3;
		
		//when
		Flux<String> namesFluxMap = fluxAndMonoGenServ.namesFluxMapFilterDoOnSomething(stringLen);
		
		//then
		StepVerifier.create(namesFluxMap)
			.expectNext("4-ALEX", "5-CHLOE")
			.verifyComplete();
	}
	
	
//	___________                           __  .__                      
//	\_   _____/__  ___ ____  ____ _______/  |_|__| ____   ____   ______
//	 |    __)_\  \/  // ___\/ __ \\____ \   __\  |/  _ \ /    \ /  ___/
//	 |        \>    <\  \__\  ___/|  |_> >  | |  (  <_> )   |  \\___ \   --- Any exception will terminate the Reactive Stream
//	/_______  /__/\_ \\___  >___  >   __/|__| |__|\____/|___|  /____  >
//	        \/      \/    \/    \/|__|                       \/     \/ 		
	
	@Test
	void exceptionFlux() {
		
		//given

		
		//when
		Flux<String> exceptionResult = fluxAndMonoGenServ.exceptionFlux();
		
		
		//then
		StepVerifier.create(exceptionResult)
			.expectNext("A", "B", "C")
			.expectError(RuntimeException.class) // Can be blank
			.verify();
	}
	
	@Test
	void exceptionFlux_2() {
		
		//given

		
		//when
		Flux<String> exceptionResult = fluxAndMonoGenServ.exceptionFlux();
		
		
		//then
		StepVerifier.create(exceptionResult)
			.expectNext("A", "B", "C")
			.expectError() // Can be blank
			.verify();
	}
	
	@Test
	void exceptionFlux_3() { // expectErrorMessage("")
		
		//given

		
		//when
		Flux<String> exceptionResult = fluxAndMonoGenServ.exceptionFlux();
		
		
		//then
		StepVerifier.create(exceptionResult)
			.expectNext("A", "B", "C")
			.expectErrorMessage("Exception Occurred")
			.verify();
	}
	
	@Test
	void exploreOnErrorReturn() {
		
		//given
		
		
		//when
		Flux<String> onErrorReturnResult = fluxAndMonoGenServ.exploreOnErrorReturn();
		
		//then
		StepVerifier.create(onErrorReturnResult)
		.expectNext("A", "B", "C", "D", "D") // Added extra .concatWith(Flux.just("D")) - To show stream continuation after recovery
//		.expectErrorMessage("Exception Occurred") // Exception is now ignored because of 'onErrorReturn'
//		.verify();// Exception is now ignored because of 'onErrorReturn'
		.verifyComplete();
		
	}
	
	
	@Test
	void exploreOnErrorResume() {
		
		//given
		IllegalStateException e = new IllegalStateException("Not a valid State");
		
		//when
		Flux<String> resumeValue = fluxAndMonoGenServ.exploreOnErrorResume(e);
		
		//then
		StepVerifier.create(resumeValue)
		.expectNext("A", "B", "C", "D", "E", "F", "G") // Added extra .concatWith(Flux.just("G")) - To show stream continuation after recovery
//		.expectErrorMessage("Exception Occurred") // Exception is now ignored because of 'onErrorResume'
//		.verify();// Exception is now ignored because of 'onErrorReturn'
		.verifyComplete();
		
	}
	
	
	@Test
	void exploreOnErrorResumeWithConditional() { // Because we used Flux.error as the "else", Stream toerminates
												 // Flux.error - Create a Flux that terminates with an error immediately
		//given
		RuntimeException e = new RuntimeException("Not a valid State");
		
		//when
		Flux<String> resumeValue = fluxAndMonoGenServ.exploreOnErrorResume(e);
		
		//then
		StepVerifier.create(resumeValue)
		.expectNext("A", "B", "C")
		.expectError(RuntimeException.class) // Stream terminated because of Flux.error
		.verify();
		
	}
	
	
	@Test
	void exploreOnErrorContinue() {
		
		//given

		
		//when
		Flux<String> resumeValue = fluxAndMonoGenServ.exploreOnErrorContinue();
		
		//then
		StepVerifier.create(resumeValue)
		.expectNext("A", "C", "D", "G") // Skips B(which we made cause the error).
		.verifyComplete();
		
	}
	
	
	@Test
	void exploreOnErrorMap() {
		
		//given

		
		//when
		Flux<String> resumeValue = fluxAndMonoGenServ.exploreOnErrorMap();
		
		//then
		StepVerifier.create(resumeValue)
		.expectNext("A") // Throws error on B & does NOT recover
		.expectError(ReactorException.class)
		.verify();
		
	}
	
	
	@Test
	void exploreDoOnError() {
		
		//given
		
		
		//when
		Flux<String> onErrorReturnResult = fluxAndMonoGenServ.exploreDoOnError();
		
		//then
		StepVerifier.create(onErrorReturnResult)
		.expectNext("A", "B", "C")
		.expectError(IllegalStateException.class) // We throw an error and stream does NOT recover
		.verify();
		
	}
	
	@Test
	void exploreOnErrorReturnMono() {
		
		//given

		
		//when
		Mono<Object> returnValue = fluxAndMonoGenServ.exploreOnErrorReturnMono();
		
		//then
		StepVerifier.create(returnValue)
		.expectNext("abc") // Mono.just("A") .map -> throws error
		.verifyComplete();
		
	}
	
	@Test
	void exploreOnErrorMapMono() {
		
		//given

		
		//when
		Mono<String> resumeValue = fluxAndMonoGenServ.exploreOnErrorMapMono();
		
		//then
		StepVerifier.create(resumeValue)
		//.expectNext("A") // Throws error on A
		.expectError(ReactorException.class)
		.verify();
		
	}
	
	
	@Test
	void exploreOnErrorContinueMono() {
		
		//given
//		String inp = "abc";
		String inp = "reactor";
		
		//when
		Mono<String> resumeValue = fluxAndMonoGenServ.exploreOnErrorContinueMono(inp);
		
		//then
		StepVerifier.create(resumeValue)
//		.expectError(ReactorException.class) // Doesn't log this part
//		.verifyComplete();
		.expectNext("reactor")
		.verifyComplete();
		
	}
	
	
	@Test
	void namesFluxContcatMap_virtualTimer() { // Makes test cases faster.  Reason?
											  // Replaces default reactor schedulers with getOrSet (time)
		
		//given
		VirtualTimeScheduler.getOrSet();
		int stringLen = 3;
		
		//when
		Flux<String> nameFluxTest = fluxAndMonoGenServ.namesFluxContcatMap(stringLen);
		
		//then
		StepVerifier.withVirtualTime(() -> nameFluxTest)
			.thenAwait(Duration.ofSeconds(10))
			.expectNext("A","L", "E", "X", "C", "H", "L", "O", "E")
//			.expectNextCount(9)
			.verifyComplete();
	}
	
	

	
//	  							   _       
//  __ _  ___ _ __   ___ _ __ __ _| |_ ___ 
// / _` |/ _ \ '_ \ / _ \ '__/ _` | __/ _ \
//| (_| |  __/ | | |  __/ | | (_| | ||  __/ // SynchronousSink - Sequential - Maintains the state throughout the whole generation of events
// \__, |\___|_| |_|\___|_|  \__,_|\__\___|
// |___/        	
	
	@Test
	void explore_generate() {
		
		//given
		
		
		//when
		Flux<Integer> generatorFluxTest = fluxAndMonoGenServ.explore_generate().log();
		
		//then
		StepVerifier.create(generatorFluxTest)
//			.expectNext(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
			.expectNextCount(10)
			.verifyComplete();
	}


// 					   _       
//  ___ _ __ ___  __ _| |_ ___ 
// / __| '__/ _ \/ _` | __/ _ \ // Asynchronous & multi-threaded
//| (__| | |  __/ (_| | ||  __/ // Generate & emit events from multiple threads
// \___|_|  \___|\__,_|\__\___| // FluxSink - onNext, onComplete, onError
	
	@Test
	void explore_create() {
		
		//given
		
		
		//when
		Flux<String> generatorFluxTest = fluxAndMonoGenServ.explore_create().log();
		
		//then
		StepVerifier.create(generatorFluxTest)
//			.expectNext("alex", "ben", "chloe", "alex", "ben", "chloe")
//			.expectNextCount(6)
			.expectNextCount(9)
			.verifyComplete();
	}
	
	
//	@Test
//	void namesFluxImm() { // Reactive Streams are immutable, only operators(like .map) attached to the datasource can transform the data
//		
//		//given
//		
//		
//		//when
//		Flux<String> namesFluxImm = fluxAndMonoGenServ.namesFluxImmutability();
//		
//		//then
//		StepVerifier.create(namesFluxImm)
//			.expectNext("ALEX") // First should be ALEX
//			.expectNext("BEN")
//			.expectNextCount(1) // 1 left after BEN
//			.verifyComplete();
//	}

}
