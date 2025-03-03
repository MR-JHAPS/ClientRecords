package com.jhaps.clientrecords.exception;

public class EntityOperationException extends RuntimeException{

	/**
	 * Should you change this value?
		    No, if you haven’t changed the class structure. Keep the generated serialVersionUID as is.
		    Yes, if you make significant changes that break compatibility. You may generate a new serialVersionUID using serialver
		     (or let the IDE generate one again).
	 */
	private static final long serialVersionUID = -6854926123849174702L;


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
