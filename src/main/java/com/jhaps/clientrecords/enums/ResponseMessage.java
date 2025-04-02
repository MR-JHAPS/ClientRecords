package com.jhaps.clientrecords.enums;

public enum ResponseMessage {
	
	SUCCESS("Successfully Executed"),
	ROLE_OBTAINED("Roles Obtained Successfully"),
	ROLE_SAVED("New Role Saved Successfully"),
	ROLE_DELETED("Role Deleted Successfully"),
	WRONG_PASSWORD("The password you entered is incorrect"),
	
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_SAVE_FAILED("Failed to save the Client"),
    UNAUTHORIZED("Unauthorized access"),
    NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
	VALIDATION_FAILED("Validation error"),
	BAD_CREDENTIALS("Credentials wrong"),
	
	UNAUTHORIZED_REQUEST("Not Authorized"),
	ACCESS_DENIED("You do not have the required authorization"),
	CLIENT_NOT_FOUND("Client Not Found"),
	CLIENT_DELETED("Client Deleted Successfully"),
	INVALID_ROLE("ROLE NOT FOUND"),
	USER_NOT_FOUND("User Not Found"),
	USER_DELETED("User Deleted Successfully"),
	DUPLICATE_DATA("Data with provided name already exists"),
	LOCKED("Your Account is locked"),
	
	USER_REGISTRATION_ERROR("Unable to Register the new user"),
	
	
	IMAGE_OBTAINED("Images Obtained Successfully"),
	INVALID_IMAGE("Image Cannot be Found in the Database."),
	IMAGE_SAVED("Image Saved Successfully."),
	IMAGE_DELETED("Image Deleted Successfully"),
	
	CLIENT_LOG_NOT_FOUND("Unable to find the Client Log"),
	
	CLIENT_BIN_NOT_FOUND(" Unable to find the Client in ClientBin"),
	ADMIN_UPDATED("Admin Updated Successfully"),
	IMAGE_DELETION_FAILED("Failed to Delete image"),
	
	CLIENT_BIN_DELETED("client is deleted from clientBin")
	;
	
	
	
	
	
	
	private final String message ;
	
	
	ResponseMessage(String message){
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	
	
}//ends Enum
