package com.liam.projectreactor.services;

import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
