package com.jhaps.clientrecords.exception;

public class EntityOperationException extends RuntimeException{

	public EntityOperationException(String operation, String entityName) {
		super ("Error on operation " + operation + " of " + entityName );
	}
	
	
	public EntityOperationException(String operation, String entityName, Throwable cause) {
		/*
		 * public RuntimeException(String message, Throwable cause) {
        	super(message, cause);
    		}
    		This is the constructor of runtime exception we are using.
		 * */
		super ("Error on operation " + operation + " of " + entityName + cause.getMessage() , cause);
	}
	
	
	
}//ends class
