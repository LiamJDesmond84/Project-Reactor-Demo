package com.liam.projectreactor.services;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureTest {
	
	
	
	@Test
	void testBackPressure() {
		
		Flux<Integer> numberRange = Flux.range(1, 100)
			.log();
		
		
//		numberRange
//			.subscribe(num -> {
//				log.info("The number is: " + num);   -  Regular unbounded subscribe()
//			});
		
		numberRange
		.subscribe(new BaseSubscriber<Integer>() {
			
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				super.hookOnSubscribe(subscription);
			}
			
			@Override
			protected void hookOnNext(Integer value) {
				super.hookOnNext(value);
				
			}
			
			@Override
			protected void hookOnComplete() {
				super.hookOnComplete();
				
			}
			
			@Override
			protected void hookOnError(Throwable throwable) {
				super.hookOnError(throwable);
			}
			
			@Override
			protected void hookOnCancel() {
				super.hookOnCancel();
			}
			
	
}

		);
			
		
	}

}
