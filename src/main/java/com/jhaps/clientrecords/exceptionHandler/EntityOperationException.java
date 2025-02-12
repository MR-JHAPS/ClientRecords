package com.jhaps.clientrecords.exceptionHandler;

public class EntityOperationException extends RuntimeException{

	public EntityOperationException(String operation, String entityName, Throwable cause) {
		/*
		 * public RuntimeException(String message, Throwable cause) {
        	super(message, cause);
    		}
    		This is the constructor of runtime exception we are using.
		 * */
		super ("Failed to " + operation + " " + entityName + cause.getMessage() , cause);
	}
	
	
	
}
