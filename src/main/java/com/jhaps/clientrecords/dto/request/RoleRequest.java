package com.jhaps.clientrecords.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleRequest {

	@NotEmpty(message = "RoleRequest: roles cannot be empty.")
	private Set<String> roles;
	
}
