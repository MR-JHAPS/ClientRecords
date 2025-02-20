package com.jhaps.clientrecords.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>{
//	@Query("SELECT c FROM Client c WHERE " +
//	           "(:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
//	           "(:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
//	           "(:dateOfBirth IS NULL OR c.dateOfBirth = :dateOfBirth) AND " +
//	           "(:postalCode IS NULL OR c.postalCode = :postalCode)")
//	    List<Client> searchClients(
//	            @Param("firstName") String firstName,
//	            @Param("lastName") String lastName,
//	            @Param("dateOfBirth") LocalDate dateOfBirth,
//	            @Param("postalCode") String postalCode
//	    );
	
	 @Query("SELECT c FROM Client c WHERE " +
	           "(:query IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
	           " LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
	           " c.postalCode =:query OR " +
	           " LOWER(c.postalCode) LIKE LOWER(CONCAT(:query, '%')) OR " +
	           " CAST(c.dateOfBirth AS string) LIKE CONCAT('%', :query, '%'))")
	    List<Client> searchClients(@Param("query") String query);
	 
	List<Client> findByFirstName(String firstName);
	List<Client> findByLastName(String lastName);
	List<Client> findByDateOfBirth(LocalDate dateOfBirth);
	List<Client> findByPostalCode(String postalCode);
	
	
	
	
}
