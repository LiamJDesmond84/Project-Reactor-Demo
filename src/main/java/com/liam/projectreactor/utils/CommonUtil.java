package com.liam.projectreactor.utils;

import static java.lang.Thread.sleep;

public class CommonUtil {
	
	public static void delay(int ms){
        try {
            sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
