package com.jhaps.clientrecords.exception;

public class EntityNotFoundException extends RuntimeException {
	//Constructor
	
	public EntityNotFoundException(String entityName , int id) {
		/*this is the constructor of the parent class :
		 *			public RuntimeException(String message) {
	        			super(message);
	    			}
		 * */
		//client + with id + 10 + not found.
		 super(entityName + " with ID " + id + " not found." );
	}
	
	public EntityNotFoundException(String entityName , int id, Throwable cause) {
		 super(entityName + " with ID " + id + " not found." ,cause );
	}
	
	public EntityNotFoundException(String entityName , String name) {
		 super(entityName + " with name " + name + " not found." );
	}
	
}
