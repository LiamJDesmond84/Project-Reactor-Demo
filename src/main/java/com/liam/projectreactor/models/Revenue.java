package com.liam.projectreactor.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Revenue {
	
	private Long movieInfoId;
    private double budget;
    private double boxOffice;

}
