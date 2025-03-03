package com.jhaps.clientrecords.response;

public enum ResponseMessage {

	
	SUCCESS("Successfully Executed"),
	DUPLICATE_ENTITY("Entity with provided name already exists"),
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_SAVE_FAILED("Failed to save the Client"),
    UNAUTHORIZED("Unauthorized access"),
    NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
	VALIDATION_FAILED("Validation error"),
	BAD_CREDENTIALS("Credentials wrong"),
	DATA_ACCESS_EXCEPTION("database Exception");
	
	
	
	private final String message ;
	
	
	ResponseMessage(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	
	
}//ends Enum
