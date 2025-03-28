package com.jhaps.clientrecords.dto;

import java.time.LocalDate;

public class UpdatedClientLogDto {

	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private String postalCode;
	
	private String userEmail;		// user who modified the client
	
	private String modificationType;  // (insert, delete, update).
	
	
}//ends class.
