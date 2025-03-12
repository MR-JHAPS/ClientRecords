package com.jhaps.clientrecords.enums;

public enum ResponseMessage {

	
	SUCCESS("Successfully Executed"),
	
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_SAVE_FAILED("Failed to save the Client"),
    UNAUTHORIZED("Unauthorized access"),
    NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
	VALIDATION_FAILED("Validation error"),
	BAD_CREDENTIALS("Credentials wrong"),
	DATA_ACCESS_EXCEPTION("database Exception"),
	
	CLIENT_NOT_FOUND("Client Not Found"),
	INVALID_ROLE("ROLE NOT FOUND"),
	USER_NOT_FOUND("User Not Found"),
	DUPLICATE_DATA("Data with provided name already exists");
	
	
	
	private final String message ;
	
	
	ResponseMessage(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	
	
}//ends Enum
