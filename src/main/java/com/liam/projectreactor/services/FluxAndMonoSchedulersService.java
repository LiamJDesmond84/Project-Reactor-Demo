package com.liam.projectreactor.services;

import java.util.List;

import reactor.core.publisher.Flux;import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

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
    	
    	Flux<String> namesFlux =  Flux.fromIterable(namesList)
    			.publishOn(Schedulers.parallel())
    			.map(this::upperCase) // with delay method below
//    			.map(x -> x.toUpperCase())
    			.log();
    	
    	Flux<String> namesFlux1 =  Flux.fromIterable(namesList1)
    			.map(this::upperCase) // with delay method below
    			.log();
    	
    	return namesFlux.mergeWith(namesFlux1);
    }
    

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
