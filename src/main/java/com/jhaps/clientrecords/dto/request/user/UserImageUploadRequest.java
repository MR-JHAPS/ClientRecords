package com.jhaps.clientrecords.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * This is a DTO Request to update a profile Picture.
 * 
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserImageUploadRequest {

	@NotBlank(message = "UserUpdateImage: imageName cannot be null.")
	private String imageName;
	
}// ends dto
