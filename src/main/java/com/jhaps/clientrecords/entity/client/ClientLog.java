package com.jhaps.clientrecords.entity.client;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ModificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Table(name="client_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="client_id", unique = true)
	private int clientId;
	
	@Column(name="client_first_name")
	private String firstName;
	
	@Column(name="client_last_name")
	private String lastName;
	
	@Column(name="client_date_of_birth")
	private LocalDate dateOfBirth;
	
	@Column(name="client_postal_code")
	private String postalCode;
	
	@Enumerated(EnumType.STRING)
	@Column(name="modification_type", nullable = false)
	private ModificationType modificationType;
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "user_email")
	private String userEmail;
	
	//When a new clientLog is created automatically adds the modified date of the client.
	//Automatically saves the updated time when the client Data  is inserted in this entity(LifeCycle Hook).
	@PrePersist
	public void onCreate() {
		this.updatedAt= LocalDateTime.now();
	}
	
	
	
} // Ends Class.
