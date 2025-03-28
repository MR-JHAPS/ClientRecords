package com.jhaps.clientrecords.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jhaps.clientrecords.dto.UpdatedClientLogDto;
import com.jhaps.clientrecords.entity.UpdatedClientLog;

@Repository
public interface UpdatedClientLogRepository extends JpaRepository<UpdatedClientLog, Integer> {

	/* I need to relate the user and client table first make this repository work */
	
	@Query("SELECT com.jhaps.clientrecords.dto.UpdatedClientLogDto " +
			"FROM UpdatedClientLog ucl " +
			"JOIN Client c"+
			"JOIN User u"
			)
	Optional<UpdatedClientLogDto> getUpdatedClientLogById(@Param(":updatedClientId")int updatedClientId);
	
	Page<UpdatedClientLogDto> getAllUpdatedClientLog(Pageable pageable);
	
	
	
	
}
