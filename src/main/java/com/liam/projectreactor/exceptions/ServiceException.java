package com.liam.projectreactor.exceptions;

public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String message;
    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(Throwable ex) {
        super(ex);
        this.message = ex.getMessage();
    }

}
