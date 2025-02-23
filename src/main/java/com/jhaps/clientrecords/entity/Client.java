package com.jhaps.clientrecords.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="clients")

@AllArgsConstructor
@NoArgsConstructor
@Data
//@Getter


public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	
	@NotBlank(message = "FirstName cannot be Blank")
	@Column(name="first_name", nullable = false)
	private String firstName;
	
	@NotBlank(message = "LastName cannot be Blank")
	@Column(name="last_name", nullable = false)
	private String lastName;
	
	@NotBlank(message = "PostalCode cannot be Blank")
	@Column(name="postal_code", nullable = false)
	private String postalCode;
	
	@NotNull(message="Date Of Birth cannot be Null")
	@Column(name="date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	
	
	
	
	
}//ends class
