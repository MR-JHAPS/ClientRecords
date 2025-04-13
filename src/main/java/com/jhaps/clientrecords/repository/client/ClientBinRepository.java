package com.jhaps.clientrecords.repository.client;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.entity.client.ClientBin;

@Repository
public interface ClientBinRepository extends JpaRepository<ClientBin, Integer>{

	Page<ClientBin> findAll(Pageable pageable);
	
	
	
	/* Directly inserting does not work it says that there is some detachment issues.*/
	/* This is the manual query to insert the client from clientBin to clientEntity with previous clientId */
	@Modifying
	@Query(value = "INSERT INTO clients (id, version, first_name, last_name, postal_code, date_of_birth, user_id) " +
	        "VALUES (:id, 0, :firstName, :lastName, :postalCode, :dob, :userId)", nativeQuery = true)
	int restoreClientFromClientBin(@Param("id") int id,
									@Param("firstName") String firstName,
									@Param("lastName") String lastName,
									@Param("postalCode") String postalCode,
									@Param("dob") LocalDate dateOfBirth,
									@Param("userId") int userId);
	
	
}
