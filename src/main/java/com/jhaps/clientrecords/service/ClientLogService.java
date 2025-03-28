package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.ClientLogDto;
import com.jhaps.clientrecords.enums.ModificationType;

public interface ClientLogService {

	void insertClientLog(int userId, int clientId, ModificationType modificationType); /* inserting the modified client for log purpose. */
	
	ClientLogDto getClientLogById(int id); /* Getting the updatedClientLog by selected UpdatedClientLogID */
	
	Page<ClientLogDto> getAllClientLog(Pageable pageable); /* @return all the updatedClientLog. */
	
	
	
	
	
}//ends interface.
