package com.jhaps.clientrecords.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/* This DTO Class is used to save the wrong password attempts by given user . */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempts {

	private String email;
	private int attempts;
	
	
}// ends class
