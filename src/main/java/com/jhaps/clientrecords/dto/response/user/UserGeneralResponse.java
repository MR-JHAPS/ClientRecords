package com.jhaps.clientrecords.dto.response.user;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneralResponse {
		
		int id;
		
		private String image = ""; // This is the profile-picture of the user.

		private String email;
		
		private LocalDateTime createdOn;
		
		private LocalDateTime updatedOn;
		
}//ends class;
