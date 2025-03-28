package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;

import com.jhaps.clientrecords.dto.UpdatedClientLogDto;
import com.jhaps.clientrecords.enums.ModificationType;

public interface UpdatedClientService {

	void insertUpdatedClientLog(int userId, int clientId, ModificationType modificationType); /* inserting the modified client for log purpose. */
	
	UpdatedClientLogDto getUpdatedClientLogById(int id); /* Getting the updatedClientLog by selected UpdatedClientLogID */
	
	Page<UpdatedClientLogDto> getAllUpdatedClientLog(); /* @return all the updatedClientLog. */
	
	
	
	
	
}//ends interface.
