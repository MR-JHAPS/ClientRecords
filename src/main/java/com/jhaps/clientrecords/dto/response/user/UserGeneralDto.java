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
public class UserGeneralDto {
		
		int id;

		private String email;
		
		private LocalDateTime createdOn;
		
		private LocalDateTime updatedOn;
		
}//ends class;
