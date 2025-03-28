package com.jhaps.clientrecords.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.dto.ClientLogDto;
import com.jhaps.clientrecords.entity.ClientLog;

@Repository
public interface ClientLogRepository extends JpaRepository<ClientLog, Integer> {

/*
 *  @user-Table : u,
 * 
 * @client-Table : c,
 * 
 * @updatedClientLog-Table : ucl;
 * 
 */
	
/*new com.jhaps.clientrecords.dto.UpdatedClientLogDto(
 * 							 "ucl.id, c.firstName, c.lastName,
 * 							 c.dateOfBirth, c.postalCode, u.email,
 * 							 ucl.modificationType, ucl.updatedAt ).
 * 
 * We are creating a new instance of : updateClientLogDto()
 * to store the each necessary field of both user and client Tables.
 */	
	
	@Query("SELECT new com.jhaps.clientrecords.dto.ClientLogDto( " +
			"ucl.id, c.firstName, c.lastName, c.dateOfBirth, c.postalCode, u.email, ucl.modificationType, ucl.updatedAt)"+
			" FROM ClientLog ucl " +
			" JOIN Client c ON ucl.clientId = c.id"+
			" JOIN User u ON ucl.userId = u.id" +
			" WHERE ucl.id = :updatedClientLogId"
			)
	Optional<ClientLogDto> getUpdatedClientLogById(@Param(":updatedClientLogId")int updatedClientId);
	
	
	
	
	
/* ----------------------------FIND ALL CLIENTLOG USING PAGEABLE.---------------------------- */	
	@Query("SELECT new com.jhaps.clientrecords.dto.ClientLogDto( " +
			"ucl.id, c.firstName, c.lastName, c.dateOfBirth, c.postalCode, u.email, ucl.modificationType, ucl.updatedAt)"+
			" FROM ClientLog ucl " +
			" JOIN Client c ON ucl.clientId = c.id"+
			" JOIN User u ON ucl.userId = u.id"
			)
	Page<ClientLogDto> getAllUpdatedClientLog(Pageable pageable);
	
	
	
	
}
