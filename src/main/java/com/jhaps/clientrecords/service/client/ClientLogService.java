package com.jhaps.clientrecords.service.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.response.ClientLogResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ModificationType;

public interface ClientLogService {

	void insertInClientLog(User user, Client client, ModificationType modificationType); /* inserting the modified client for log purpose. */
	
	ClientLogResponse getClientLogById(int clientLogId); /* Getting the updatedClientLog by selected UpdatedClientLogID */
	
	Page<ClientLogResponse> getAllClientLog(Pageable pageable); /* @return all the updatedClientLog. */
	
	
	
	
	
}//ends interface.
