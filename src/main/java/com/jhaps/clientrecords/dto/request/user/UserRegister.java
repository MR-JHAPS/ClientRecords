package com.jhaps.clientrecords.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This DTO is responsible for the registration of the user.
 * 
 */



@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegister {

	@Email
	@NotBlank(message = "User email cannot be blank")
	private String email;
	
	@NotBlank
	@Size(min = 4, max = 100)
	private String password;
	
	@NotBlank
	@Size(min = 4, max = 100)
	private String confirmPassword;
}
