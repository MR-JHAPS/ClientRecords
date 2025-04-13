package com.jhaps.clientrecords.entity.client;

import java.time.LocalDate;

import com.jhaps.clientrecords.entity.system.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @usage: This Entity stores the clients that are deleted from "@entity: Client".
 * 
 */




@Entity
@Table(name = "client_bin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientBin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	/*These are all the info of the client so that i can restore it back in client table with original clientId if i want to
	 * or incase of accidental deletion.	
	 */
	@Column(name="client_id", nullable = false, unique = true)
	private int clientId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="date_of_birth")
	private LocalDate dateOfBirth;
	
	@Column(name="postal_code")
	private String postalCode;
	
	
	@Column(name="user_id")
	private int userId;
	
	
}// ends entity
