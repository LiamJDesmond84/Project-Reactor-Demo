package com.liam.projectreactor.exceptions;

public class NetworkException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String message;

	public NetworkException(String message) {
        super(message);
        this.message = message;
    }

    public NetworkException(Throwable ex) {
        super(ex);
        this.message = message;
    }

}
