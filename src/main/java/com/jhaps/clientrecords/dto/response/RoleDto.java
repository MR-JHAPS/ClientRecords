package com.jhaps.clientrecords.dto.response;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

	
	@NotEmpty(message = "RoleNames cannot be empty in RoleDto.")
	private Set<String> roleNames;
	
	
	
}
