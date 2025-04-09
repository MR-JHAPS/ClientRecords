package com.jhaps.clientrecords.repository.client;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.dto.response.ClientLogResponse;
import com.jhaps.clientrecords.entity.client.ClientLog;

@Repository
public interface ClientLogRepository extends JpaRepository<ClientLog, Integer> {

	
	/*	new com.jhaps.clientrecords.dto.UpdatedClientLogDto(
	 * 											cl.id, cl.clientId, cl.firstName, cl.lastName, cl.dateOfBirth,
	 * 								 			cl.postalCode, u.email, cl.modificationType, cl.updatedAt
	 * 											 )
	 * 
	 * We are creating a new instance of : updateClientLogDto()
	 * to store the each necessary field of both user and clientLog Tables.
	 */	
	
/* ----------------------------FIND CLIENTLOG By ClientLogID.---------------------------- */		
	
	@Query("SELECT new com.jhaps.clientrecords.dto.response.ClientLogResponse(" +
			"cl.id, cl.clientId, cl.firstName, cl.lastName, cl.dateOfBirth, cl.postalCode, cl.userEmail, cl.modificationType, cl.updatedAt) "+
			"FROM ClientLog cl " +
			"WHERE cl.id= :clientLogId"
		)
	Optional<ClientLogResponse> getClientLogById(@Param("clientLogId")int clientLogId);
	
	
/* ----------------------------FIND ALL CLIENTLOG USING PAGEABLE.---------------------------- */	
	
	@Query("SELECT new com.jhaps.clientrecords.dto.response.ClientLogResponse( " +
			"cl.id, cl.clientId, cl.firstName, cl.lastName, cl.dateOfBirth, cl.postalCode, cl.userEmail, cl.modificationType, cl.updatedAt) "+
			"FROM ClientLog cl ")
	Page<ClientLogResponse> getAllClientLog(Pageable pageable);
}
