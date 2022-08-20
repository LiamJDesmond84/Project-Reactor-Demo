package com.liam.projectreactor.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

@Slf4j
public class BackpressureTest {
	
	
	
	@Test
	void testBackPressure() { // Just showing backpressure
		
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
//				super.hookOnSubscribe(subscription);
				request(2); // Just requesting 2 elements

			}
			
			@Override
			protected void hookOnNext(Integer value) {
//				super.hookOnNext(value);
				log.info("Hook onNext: {}", value);
				if (value == 1) {
					cancel();
				}
			}
			
			@Override
			protected void hookOnComplete() {
//				super.hookOnComplete();		
			}
			
			@Override
			protected void hookOnError(Throwable throwable) {
//				super.hookOnError(throwable);
			}
			
			@Override
			protected void hookOnCancel() {
//				super.hookOnCancel();
				log.info("Inside of cancel");
			}
			
		});
		
	}
	
	
	@Test
	void testBackPressure_1() throws InterruptedException {
		
		Flux<Integer> numberRange = Flux.range(1, 100)
			.log();
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		numberRange
		.subscribe(new BaseSubscriber<Integer>() {
			
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(2); // Just requesting 2 elements

			}
			
			@Override
			protected void hookOnNext(Integer value) {
				log.info("Hook onNext: {}", value);
				if (value %2 == 0 || value < 50) {
					request(2);
				}
				else {
					cancel();
				}
			}
			
			@Override
			protected void hookOnComplete() {		
			}
			
			@Override
			protected void hookOnError(Throwable throwable) {
			}
			
			@Override
			protected void hookOnCancel() {
				log.info("Inside of cancel");
				latch.countDown();
			}
			
		});
		
		assertTrue(latch.await(5L, TimeUnit.SECONDS)); // Latch stays open for 5(5L) seconds
		
	}
	
	
	@Test
	void testBackPressure_drop() throws InterruptedException {
		
		Flux<Integer> numberRange = Flux.range(1, 100)
			.log();
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		
		
		numberRange
		.onBackpressureDrop(item -> {
			log.info("Dropped items are: " + item);
		})
		.subscribe(new BaseSubscriber<Integer>() {
			
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(2); // Just requesting 2 elements

			}
			
			@Override
			protected void hookOnNext(Integer value) {
				log.info("Hook onNext: {}", value);
//				if (value %2 == 0 || value < 50) {
//					request(2);
//				}
//				else {
//					cancel();
//				}
				
				if(value == 2) {
					hookOnCancel();
				}
			}
			
			@Override
			protected void hookOnComplete() {		
			}
			
			@Override
			protected void hookOnError(Throwable throwable) {
			}
			
			@Override
			protected void hookOnCancel() {
				log.info("Inside of cancel");
				latch.countDown();
			}
			
		});
		
		assertTrue(latch.await(5L, TimeUnit.SECONDS)); // Latch stays open for 5(5L) seconds
		
	}
	
	
	
	@Test
	void testBackPressure_buffer() throws InterruptedException {
		
		Flux<Integer> numberRange = Flux.range(1, 100)
			.log();
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		
		
		numberRange
		.onBackpressureBuffer(10, item -> { // buffers #(10, holds 11 elements(index starts at 0) after # of requests(value < 50 including request(1))
			log.info("Last buffer element is: " + item); // last element of the buffer
		})
		.subscribe(new BaseSubscriber<Integer>() {
			
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(1); // Just requesting 1 element

			}
			
			@Override
			protected void hookOnNext(Integer value) {
				log.info("Hook onNext: {}", value);
				
				if(value < 50) {
					request(1);
				}
				else {
					hookOnCancel();
				}
			}
			
			@Override
			protected void hookOnComplete() {		
			}
			
			@Override
			protected void hookOnError(Throwable throwable) {
			}
			
			@Override
			protected void hookOnCancel() {
				log.info("Inside of cancel");
				latch.countDown();
			}
			
		});
		
		assertTrue(latch.await(5L, TimeUnit.SECONDS)); // Latch stays open for 5(5L) seconds
		
	}
	
	
	@Test
	void testBackPressure_error() throws InterruptedException { // Throws an OverFlowException when the publisher sends more data than the subscriber's requested amount
		
		Flux<Integer> numberRange = Flux.range(1, 100)
			.log();
		
		
		CountDownLatch latch = new CountDownLatch(1);
		
		
		
		numberRange
		.onBackpressureError()
		.subscribe(new BaseSubscriber<Integer>() {
			
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(1); // Just requesting 1 element

			}
			
			@Override
			protected void hookOnNext(Integer value) {
				log.info("Hook onNext: {}", value);
				
				if(value < 50) {
					request(1);
				}
				else {
					hookOnCancel();
				}
			}
			
			@Override
			protected void hookOnComplete() {		
			}
			
			@Override
			protected void hookOnError(Throwable throwable) {
				log.error("Exception is: " + throwable);
				// "The receiver is overrun by more signals than expected" - more than 50
				// if you want to throw an exception if the producer offers more elements than the subscriber can process
			}
			
			@Override
			protected void hookOnCancel() {
				log.info("Inside of cancel");
				latch.countDown();
			}
			
		});
		
		assertTrue(latch.await(5L, TimeUnit.SECONDS)); // Latch stays open for 5(5L) seconds
		
	}
	

}
