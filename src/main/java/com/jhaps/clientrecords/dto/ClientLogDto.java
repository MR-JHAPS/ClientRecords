package com.jhaps.clientrecords.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jhaps.clientrecords.enums.ModificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientLogDto {

	private int id; // this is clientLog id
	
	private int clientId;	// this is client Id.
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private String postalCode;
	
	private String userEmail;		// user who modified the client
	
	private ModificationType modificationType;  // (insert, delete, update).
	
	private LocalDateTime updatedAt;
	
	
}//ends class.
