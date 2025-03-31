package com.jhaps.clientrecords.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientResponse {
	private int id;
	
	@Schema(description = "client's first name")
	@NotBlank(message = "FirstName cannot be Blank")
	private String firstName;
	
	@NotBlank(message = "LastName cannot be Blank")
	private String lastName;
	
	@NotNull(message="Date Of Birth cannot be Null")
	private LocalDate dateOfBirth;
	
	@NotBlank(message = "PostalCode cannot be Blank")
	private String postalCode;
}
