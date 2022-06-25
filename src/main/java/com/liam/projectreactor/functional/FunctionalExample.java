package com.liam.projectreactor.functional;


import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {
	
public static void main(String[] args) {
		
		
		List<String> namesList = List.of("Alex", "Ben", "Chloe", "Adam", "Adam");
		
		List<String> result = namesGreaterThanSize(namesList, 3);
		System.out.println(result);
		

	}
	
	private static List<String> namesGreaterThanSize(List<String> namesList, int len) {
		
		return namesList
			.stream()
			.filter(s->s.length() > len)
			.map(String::toUpperCase)
			.distinct()
			.sorted()
			.collect(Collectors.toList());
		
	}

}
