package com.jhaps.clientrecords.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempts {

	private String email;
	private int attempts;
	
	
}// ends class
