package com.liam.projectreactor.imperative;

import java.util.ArrayList;
import java.util.List;

public class ImperativeExample {
	
	public static void main(String[] args) {
		
		
		List<String> namesList = List.of("Alex", "Ben", "Chloe", "Adam", "Adam");
		
		List<String> result = namesGreaterThanSize(namesList, 3);
		System.out.println(result);
		

	}
	
	private static List<String> namesGreaterThanSize(List<String> namesList, int len) {
		
		List<String> newNamesList = new ArrayList<String>();
		
		for(int x = 0; x < namesList.size(); x++) {
			
			if(namesList.get(x).length() > len && !newNamesList.contains(namesList.get(x))) {
				newNamesList.add(namesList.get(x).toUpperCase());
			}
			
		}
		
//		for(String name: namesList) {
//			if(name.length() > len && !newNamesList.contains(name)) {
//				newNamesList.add(name);
//			}
//			
//		}
		
		return newNamesList;
		
	}

}
