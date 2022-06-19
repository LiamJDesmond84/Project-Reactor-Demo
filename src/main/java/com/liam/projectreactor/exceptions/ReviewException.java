package com.liam.projectreactor.exceptions;

public class ReviewException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String message;
	
    public ReviewException(String message) {
        super(message);
        this.message = message;
    }

}
