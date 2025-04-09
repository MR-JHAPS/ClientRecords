package com.jhaps.clientrecords.repository.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.system.User;

import jakarta.transaction.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{

	
	 Page<Client> findAll(Pageable pageable);
	
	 @Query("SELECT c FROM Client c WHERE " +
	           "(:query IS NULL OR"+
	           " LOWER(c.firstName) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " LOWER(c.lastName) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " c.postalCode =:query OR " +
	           " LOWER(c.postalCode) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " CAST(c.dateOfBirth AS string) LIKE CONCAT('%', :query, '%'))")
    Page<Client> searchClients(@Param("query") String query, Pageable pageable);
	 
	
	 Page<Client> findByFirstNameStartingWithIgnoreCase(String firstName, Pageable pageable);
	
	Page<Client> findByLastNameStartingWithIgnoreCase(String lastName, Pageable pageable);
	
	Page<Client> findByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);
	
	Page<Client> findByPostalCodeStartingWithIgnoreCase(String postalCode, Pageable pageable);
	
	List<Client> findByUser_Id(int id);
	
	List<Client> findByUser_Email(String userEmail);
	
	/* Updating clients User by ID of user and Admin. */
	@Transactional
	@Modifying
	@Query("UPDATE Client c SET c.user.id=:adminID WHERE c.user.id=:userID")
	int bulkReassignClientsById(@Param("userID") int userID, @Param("adminID") int adminID);
	
	
	/*Updating clients User by Email of user and admin since User.Email are unique.*/
	@Transactional
	@Modifying
	@Query("UPDATE Client c SET c.user.email=:adminEmail WHERE c.user.email=:userEmail")
	int bulkReassignClientsByEmail(@Param("userEmail") String userEmail, @Param("adminEmail") String adminEmail);
	
	
	
	
}
