package com.jhaps.clientrecords.repository.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.client.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{

	
//	boolean existsByEmail(String email);
	
	 Page<Client> findAll(Pageable pageable);
	
	 @Query("SELECT c FROM Client c WHERE " +
	           "(:query IS NULL OR"+
	           " LOWER(c.firstName) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " LOWER(c.lastName) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " c.postalCode =:query OR " +
	           " LOWER(c.postalCode) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " CAST(c.dateOfBirth AS string) LIKE CONCAT('%', :query, '%'))")
    Page<Client> searchClients(@Param("query") String query, Pageable pageable);
	 
	
//	 @Query("SELECT c from Client c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT(:firstName,'%'))")
//	Page<Client> findByFirstName(@Param("firstName") String firstName, Pageable pageable);
	 //OR
	 Page<Client> findByFirstNameStartingWithIgnoreCase(String firstName, Pageable pageable);
	
	Page<Client> findByLastNameStartingWithIgnoreCase(String lastName, Pageable pageable);
	
	Page<Client> findByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);
	
	Page<Client> findByPostalCodeStartingWithIgnoreCase(String postalCode, Pageable pageable);
	
	
	
	
}
