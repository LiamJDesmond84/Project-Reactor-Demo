package com.liam.projectreactor.services;

import com.liam.projectreactor.models.Revenue;

import static com.liam.projectreactor.utils.CommonUtil.delay;

public class RevenueService {
	
	public Revenue getRevenue(Long movieId){
        delay(1000); // simulating a network call ( DB or Rest call)
        return Revenue.builder()
                .movieInfoId(movieId)
                .budget(1000000)
                .boxOffice(5000000)
                .build();

    }

}
