package com.jhaps.clientrecords.exception.system;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class CustomBadCredentialsException extends RuntimeException{

	private int remainingAttempts;
	
	/**
	 * Constructor
	 */
	public CustomBadCredentialsException(String message, int remainingAttempts){
		
		super(message);
		this.remainingAttempts = remainingAttempts;
		
	}
	
	
	
}
