package com.liam.projectreactor.imperative;

import java.util.ArrayList;
import java.util.List;

public class ImperativeExample {
	
	public static void main(String[] args) {
		
		
		List<String> namesList = List.of("Alex", "Ben", "Chloe", "Adam");
		
		List<String> result = namesGreaterThanSize(namesList, 3);
		System.out.println(result);
		

	}
	
	private static List<String> namesGreaterThanSize(List<String> namesList, int x) {
		
		List<String> newNamesList = new ArrayList<String>();
		
		for(int y = 0; y < namesList.size(); y++) {
			
			if(namesList.get(y).length() > 3) {
				newNamesList.add(namesList.get(y));
			}
			
		}
		
//		for(String name: namesList) {
//			if(name.length() > 3) {
//				newNamesList.add(name);
//			}
//			
//		}
		
		return newNamesList;
		
	}

}
