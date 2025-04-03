package com.jhaps.clientrecords.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSaveRequest {

	@NotBlank(message = "RoleCreateRequest: roleName cannot be empty")
	private String role;
	
}
