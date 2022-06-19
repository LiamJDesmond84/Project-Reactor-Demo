package com.liam.projectreactor.services;

import java.util.List;
import static com.liam.projectreactor.utils.CommonUtil.delay;

public class FluxAndMonoSchedulersService {
	
	static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
