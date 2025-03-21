package com.jhaps.clientrecords.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private int id;
	
	@NotBlank(message = "Email cannot be empty")
	@Email(message ="Email Format is not correct")
	private String email;
	
	@NotBlank(message = "Password cannot be empty")
	private String password;
	
//	private int attempts; // this is to know the number of attempts made to log in .(after certain time the id will be blocked).
//	
//	private String oldPassword; // this is for the verification before updating the user
	
	
	
}//ends class
