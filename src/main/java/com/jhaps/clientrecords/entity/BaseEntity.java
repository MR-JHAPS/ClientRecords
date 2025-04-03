package com.jhaps.clientrecords.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor

@SuperBuilder // this is to build an object of this class .build().createdAt(...).updatedAt(...)
@MappedSuperclass // means it itself is not an entity and won't be present in Database but if extended by other entity it will be displayed in that entity table.

public class BaseEntity {

	@Column(updatable = false, insertable = true)
	private LocalDateTime createdOn;
	
	@Column(insertable = false, updatable = true)
	private LocalDateTime updatedOn;
	
	@PrePersist
	public void createdAt() {
		this.createdOn = LocalDateTime.now();
	}
	
	@PreUpdate
	public void updatedAt() {
		this.updatedOn = LocalDateTime.now();
	}
	
	
	
	
}
