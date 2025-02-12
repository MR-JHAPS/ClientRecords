package com.jhaps.clientrecords.exceptionHandler;

public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(String entityName , int id) {
		//this is the constructor of the parent class
		/*	public RuntimeException(String message) {
	        	super(message);
	    	}
		 * 
		 * */
		 super(entityName + " with ID " + id + " not found." );
	}
	
}
