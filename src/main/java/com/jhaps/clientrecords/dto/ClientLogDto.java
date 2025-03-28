package com.jhaps.clientrecords.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientLogDto {

	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private String postalCode;
	
	private String userEmail;		// user who modified the client
	
	private String modificationType;  // (insert, delete, update).
	
	private LocalDateTime updatedAt;
	
	
}//ends class.
