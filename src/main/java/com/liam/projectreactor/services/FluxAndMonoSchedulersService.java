package com.liam.projectreactor.services;

import java.util.List;

import reactor.core.publisher.Flux;

import static com.liam.projectreactor.utils.CommonUtil.delay;

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
    	
    	return Flux.fromIterable(namesList)
//    			.map(this::upperCase)
    			.map(x -> x.toUpperCase())
    			.log();
    }
    

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
