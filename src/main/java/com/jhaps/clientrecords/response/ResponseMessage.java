package com.jhaps.clientrecords.response;

public enum ResponseMessage {

	
	SUCCESS("Successfully Executed"),
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_SAVE_FAILED("Failed to save the Client"),
    UNAUTHORIZED("Unauthorized access"),
    NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error");
	
	
	
	private final String message ;
	
	
	ResponseMessage(String message){
		this.message = message;
	}
	
	public String getMesage() {
		return this.message;
	}
	
	
	
	
}//ends Enum
