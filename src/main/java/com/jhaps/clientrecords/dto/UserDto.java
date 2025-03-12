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
	
	
	
	
}//ends class
