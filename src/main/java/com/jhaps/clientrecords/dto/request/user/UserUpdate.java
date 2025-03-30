package com.jhaps.clientrecords.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This DTO is the Request used to update the user Data. it contains current password
 * 
 * @currentPassword will be required to modify the data(i.e : changing email | password).
 * 
 * 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdate {

	@NotBlank(message = "UserUpdate 'Email' cannot be empty")
	@Email
	private String email;
	
	@NotBlank(message = "UserUpdate 'currentPassword' cannot be empty")
	private String currentPassword;
	
	//optional
	@Size(min=4, max=100 ,message = "UserUpdate 'newPasswordv' must be between 4-100 characters")
	private String newPassword;
	
	//optional
	@Size(min=4, max=100 ,message = "UserUpdate 'confirmPassword' must be between 4-100 characters")
	private String confirmPassword;
	
	
	// Custom validation (called via @Valid)
	public void validate() {
		if(newPassword!=null && !newPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException("New password and confirmation must match");
		}
	}
	
	
}
