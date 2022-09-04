package com.liam.projectreactor.services;

import java.time.Duration;
//import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
import static com.liam.projectreactor.utils.CommonUtil.delay;

@Slf4j
public class ColdAndHotPublisherTest {
	
	
	@Test
	void coldPublisherTest() {
		
//		Flux<Integer> fluxRange = Flux.range(1, 10)
//				.flatMap(x -> Mono.just(x)
//					.subscribeOn(Schedulers.parallel())
//
//				).log();
//		
//		Flux<Integer> fluxRange2 = Flux.range(1, 10)
//				.flatMap(x -> Mono.just(x)
//					.subscribeOn(Schedulers.parallel())
//
//				).log();
//		
//		List<Integer> intRange = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//		
//		
//		fluxRange.subscribe(x -> System.out.println("Subscriber 1: " + x));
//		
//		fluxRange2.subscribe(x -> System.out.println("Subscriber 2: " + x));
//		
//		Flux<Integer> fluxRange1 = Flux.fromIterable(intRange)
//				.flatMap(x -> 
//					Mono.just(x)
//					.subscribeOn(Schedulers.parallel())
//
//				)
//				.map(x -> {
//					log.info("Subscriber 3: " + x);
//					return x;
//				});
//		
//		fluxRange1.subscribe(x -> System.out.println("Subscriber 4: " + x));
		
		
		Flux<Integer> fluxRange = Flux.range(1, 10);
		
		fluxRange.subscribe(x -> System.out.println("Subscriber 1: " + x));
		
		fluxRange.subscribe(x -> System.out.println("Subscriber 2: " + x));
				
	}
	
	
	@Test
	void hotPublisherTest() {
		
		Flux<Integer> fluxRange = Flux.range(1, 10)
				.delayElements(Duration.ofSeconds(1));
		
		
		ConnectableFlux<Integer> connectableFlux = fluxRange.publish();
		
		connectableFlux.connect(); // now behaves like a hotstream WITH publish as well
		
		connectableFlux.subscribe(x -> System.out.println("Subscriber 1: " + x));
		delay(4000); // So that Subscriber 2 starts subscribing AFTER the elements start emitting

		connectableFlux.subscribe(x -> System.out.println("Subscriber 2: " + x));
		delay(10000); // Just allowing 10 seconds for all 10 elements to emit
		

	}
	
	
	@Test
	void hotPublisherTest_autoConnect() { // Set a minimum # of subscribers before values are emitted
		
		Flux<Integer> fluxRange = Flux.range(1, 10)
				.delayElements(Duration.ofSeconds(1));
		
		
		Flux<Integer> hotSource = fluxRange.publish()
				.autoConnect(2); // source only starts emitting values after 2 subscribers are connected
		
		
		
		hotSource.subscribe(x -> System.out.println("Subscriber 1: " + x));
		delay(2000); // So that Subscriber 2 starts subscribing AFTER the elements start emitting

		hotSource.subscribe(x -> System.out.println("Subscriber 2: " + x));
		System.out.println("2 Subscribers are connected");
		delay(3000);
		
		hotSource.subscribe(x -> System.out.println("Subscriber 3: " + x)); // Subscriber 3 starts 3 seconds into the hot stream
		delay(10000); // Just allowing 10 seconds for all 10 elements to emit
		

	}
	
	
	@Test
	void hotPublisherTest_refCount() { // Set a minimum # of subscribers before values are emitted, but will stop emitting if that count drops to 0.
									   // Will start from beginning if n(2) # of subscribers are subscribed.
		
		Flux<Integer> fluxRange = Flux.range(1, 10)
				.delayElements(Duration.ofSeconds(1))
				.doOnCancel(() -> log.info("Received cancel signal")); // doOnCanel logging when dispose() runs
		
		
		Flux<Integer> hotSource = fluxRange.publish()
				.refCount(2); // source only starts emitting values after 2 subscribers are connected & will stop if count drops to 0.
		
		
		
		Disposable disposable1 =  hotSource.subscribe(x -> System.out.println("Subscriber 1: " + x));
		delay(2000); // So that Subscriber 2 starts subscribing AFTER the elements start emitting

		Disposable disposable2 = hotSource.subscribe(x -> System.out.println("Subscriber 2: " + x));
		System.out.println("2 Subscribers are connected");
		delay(3000);

		System.out.println("Disposing 1");
		disposable1.dispose();
		
		System.out.println("Disposing 2");
		disposable2.dispose();

		System.out.println("Subscribing with 3");
		hotSource.subscribe(x -> System.out.println("Subscriber 3: " + x));
		delay(2000);
		
		System.out.println("Subscribing with 4");
		hotSource.subscribe(x -> System.out.println("Subscriber 4: " + x));
		delay(10000); // Just allowing 10 seconds for all 10 elements to emit
		

	}

}
