package com.jhaps.clientrecords.dto.request.user;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *	This DTO is the Request to update for Admin 
 * 
 */


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUpdateRequest {

	
	@NotBlank(message = "AdminUpdate 'Email' cannot be empty")
	@Email
	private String email;
	
	@NotBlank(message = "AdminUpdate 'currentPassword' cannot be empty")
	private String currentPassword;
	
	//optional
	@Size(min=4, max=100 ,message = "AdminUpdate 'newPasswordv' must be between 4-100 characters")
	private String newPassword;
	
	//optional
	@Size(min=4, max=100 ,message = "AdminUpdate 'confirmPassword' must be between 4-100 characters")
	private String confirmPassword;
	
	@NotEmpty( message ="AdminUpdate 'roles' cannot be empty ")
	private Set<String> roles;
	
	// Custom validation (called via @Valid)
//	public void validate() {
//		if (newPassword != null && !newPassword.equals(confirmPassword)) {
//            throw new IllegalArgumentException("New password and confirmation must match");
//        }
//	}
//	
	
	
}
