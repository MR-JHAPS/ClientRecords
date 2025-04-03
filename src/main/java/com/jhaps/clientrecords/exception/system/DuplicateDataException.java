package com.jhaps.clientrecords.exception.system;

public class DuplicateDataException extends RuntimeException {

	
	
	public DuplicateDataException(String fieldName) {
		super (fieldName + " already exists. Can't create Duplicate");
	}
	

}//ends class
