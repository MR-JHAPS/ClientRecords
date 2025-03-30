package com.jhaps.clientrecords.dto.response.user;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * User Response DTO  for "User-Profile".
 * 
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {

	 	private Long id;
	    private String email;
	    private LocalDateTime createdAt;
	    private LocalDateTime updatedAt;
}
