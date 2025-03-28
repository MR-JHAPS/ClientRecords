package com.jhaps.clientrecords.entity;

import java.time.LocalDateTime;

import com.jhaps.clientrecords.enums.ModificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="updated_client_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="client_id")
	private int clientId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="modification_type", nullable = false)
	private ModificationType modificationType;
	
	@Column(name="updated_at")
	private LocalDateTime updatedAt;
	
	//Automatically saves the updated time when the data is inserted in this entity(LifeCycle Hook).
	@PrePersist
	public void onCreate() {
		this.updatedAt= LocalDateTime.now();
	}
	
	
	
} // Ends Class.
