package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.ClientLogDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.enums.ModificationType;

public interface ClientLogService {

	void insertInClientLog(User user, Client client, ModificationType modificationType); /* inserting the modified client for log purpose. */
	
	ClientLogDto getClientLogById(int clientLogId); /* Getting the updatedClientLog by selected UpdatedClientLogID */
	
	Page<ClientLogDto> getAllClientLog(Pageable pageable); /* @return all the updatedClientLog. */
	
	
	
	
	
}//ends interface.
