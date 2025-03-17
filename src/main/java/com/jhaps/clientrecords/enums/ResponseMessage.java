package com.jhaps.clientrecords.enums;

public enum ResponseMessage {

	
	SUCCESS("Successfully Executed"),
	ROLE_OBTAINED("Roles Obtained Successfully"),
	ROLE_SAVED("New Role Saved Successfully"),
	ROLE_DELETED("Role Deleted Successfully"),
	
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_SAVE_FAILED("Failed to save the Client"),
    UNAUTHORIZED("Unauthorized access"),
    NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
	VALIDATION_FAILED("Validation error"),
	BAD_CREDENTIALS("Credentials wrong"),
	
	ACCESS_DENIED("You do not have the required authorization"),
	CLIENT_NOT_FOUND("Client Not Found"),
	CLIENT_DELETED("Client Deleted Successfully"),
	INVALID_ROLE("ROLE NOT FOUND"),
	USER_NOT_FOUND("User Not Found"),
	USER_DELETED("User Deleted Successfully"),
	DUPLICATE_DATA("Data with provided name already exists");
	
	
	
	private final String message ;
	
	
	ResponseMessage(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	
	
}//ends Enum
