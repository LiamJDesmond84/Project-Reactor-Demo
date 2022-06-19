package com.liam.projectreactor.exceptions;

public class MovieException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String message;
    public MovieException(String message) {
        super(message);
        this.message = message;
    }

    public MovieException(Throwable ex) {
        super(ex);
        this.message = ex.getMessage();
    }

}
