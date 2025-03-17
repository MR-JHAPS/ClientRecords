package com.jhaps.clientrecords.exception;

public class DuplicateDataException extends RuntimeException {

	
	
	public DuplicateDataException(String fieldName) {
		super (fieldName + " already exists. Can't create Duplicate");
	}
	
	
//	public DuplicateDataException(String fieldName, Throwable cause) {
//		super (fieldName + " already exists. Cause : " + cause.getMessage(), cause);
//	}
//	
}//ends class
