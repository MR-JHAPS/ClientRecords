package com.jhaps.clientrecords.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientBinDto {

	private int id ;
	
	private int clientId;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDateTime dateOfBirth;
	
	private String postalCode;
	
	
	
}
