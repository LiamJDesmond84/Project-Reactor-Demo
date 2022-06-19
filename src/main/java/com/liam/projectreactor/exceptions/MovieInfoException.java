package com.liam.projectreactor.exceptions;

public class MovieInfoException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String message;

    public MovieInfoException(String message) {
        super(message);
        this.message = message;

    }

}
