package com.jhaps.clientrecords.enums;

import org.springframework.dao.DataIntegrityViolationException;

public enum ResponseMessage {
	
	
	
    UNAUTHORIZED("Unauthorized access"),
    NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
	VALIDATION_FAILED("Validation error"),
	BAD_CREDENTIALS("Credentials wrong"),
	UNAUTHORIZED_REQUEST("Not Authorized"),
	ACCESS_DENIED("You do not have the required authorization"),
	DUPLICATE_DATA("Data with provided name already exists"),
	LOCKED("Your Account is locked"),
	WRONG_ARGUMENT(" Wrong argument passed"),
	WRONG_PASSWORD("The password you entered is incorrect"),	
	SUCCESS("Successfully Executed"),
	DATA_INTEGRITY_VIOLATION("DataIntegrityViolation"),
	
	
	ROLE_OBTAINED("Roles Obtained Successfully"),
	ROLE_SAVED("New Role Saved Successfully"),
	ROLE_DELETED("Role Deleted Successfully"),
	INVALID_ROLE("ROLE NOT FOUND"),
	
    CLIENT_ALREADY_EXISTS("Client already exists"),
    CLIENT_SAVE_FAILED("Failed to save the Client"),
    CLIENT_DELETE_FAILED("Failed to delete the clients"),
    CLIENT_NOT_FOUND("Client Not Found"),
	CLIENT_DELETED("Client Deleted Successfully"),
	
	CLIENT_BIN_NOT_FOUND(" Unable to find the Client in ClientBin"),
	CLIENT_BIN_DELETED("client is deleted from clientBin"),
	
	CLIENT_LOG_NOT_FOUND("Unable to find the Client Log"),
	
	USER_NOT_FOUND("User Not Found"),
	USER_DELETED("User Deleted Successfully"),
	USER_REGISTRATION_ERROR("Unable to Register the new user"),
	ADMIN_UPDATED("Admin Updated Successfully"),
	
	FILE_OBTAINED("UserFile Obtained Successfully"),
	INVALID_FILE("UserFile Cannot be Found in the Database."),
	FILE_SAVED("UserFile Saved Successfully."),
	FILE_DELETED("UserFile Deleted Successfully"),
	FILE_DELETION_FAILED("Failed to Delete UserFile"),
	FILE_ERROR("Error while handling UserFile"),
	
//	IMAGE_OBTAINED("Images Obtained Successfully"),
//	INVALID_IMAGE("Image Cannot be Found in the Database."),
//	IMAGE_SAVED("Image Saved Successfully."),
//	IMAGE_DELETED("Image Deleted Successfully"),
//	IMAGE_DELETION_FAILED("Failed to Delete image"),
//	IMAGE_ERROR("Error while handling Image File"),	
	;
	
	
	
	
	
//	field
	private final String message ;
	
	
//	Constructor
	ResponseMessage(String message){
		this.message = message;
	}
	
//	method
	public String getMessage() {
		return this.message;
	}
	
	
	
	
}//ends Enum
