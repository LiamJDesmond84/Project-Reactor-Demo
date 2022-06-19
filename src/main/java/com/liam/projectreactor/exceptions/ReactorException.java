package com.liam.projectreactor.exceptions;

public class ReactorException extends Throwable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Throwable exception;
    private String message;

    public ReactorException(Throwable exception, String message) {
        this.exception = exception;
        this.message = message;

    }

}
