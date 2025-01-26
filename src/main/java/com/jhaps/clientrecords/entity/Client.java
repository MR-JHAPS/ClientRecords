package com.jhaps.clientrecords.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	
	@Column(name="first_name", nullable = false)
	private String firstName;
	
	@Column(name="last_name", nullable = false)
	private String lastName;
	
	@Column(name="postal_code", nullable = false)
	private String postalCode;
	
	@Column(name="date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	
	
//	
//	public String getFirstName() {
//		return this.firstName;
//	}
	
	
	
	
}
