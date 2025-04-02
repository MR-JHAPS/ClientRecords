package com.jhaps.clientrecords.serviceImpl.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.response.ClientLogResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientLog;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ModificationType;
import com.jhaps.clientrecords.exception.client.ClientLogNotFoundException;
import com.jhaps.clientrecords.repository.client.ClientLogRepository;
import com.jhaps.clientrecords.service.client.ClientLogService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ClientLogServiceImpl implements ClientLogService{

	private ClientLogRepository clientLogRepo;
	
	
	@Transactional
	@Override
	public void insertInClientLog(User user, Client client, ModificationType modificationType) {
		log.info("Action: Inserting the Client_id: {} with FirstName: {} in the ClientLog", client.getId(), client.getFirstName());
		ClientLog clientLog = new ClientLog();
		clientLog.setUser(user);
		clientLog.setClientId(client.getId());
		clientLog.setFirstName(client.getFirstName());
		clientLog.setLastName(client.getLastName());
		clientLog.setDateOfBirth(client.getDateOfBirth());
		clientLog.setPostalCode(client.getPostalCode());
		clientLog.setModificationType(modificationType);
		log.info("Action: Client with ID: {} successfully logged in ClientLog.", client.getId());
		clientLogRepo.save(clientLog);
	}
	
	

	
	@Override
	public ClientLogResponse getClientLogById(int clientLogId) {
		log.info("Action: Getting clientLog by clientLog_ID: {}.", clientLogId);
		ClientLogResponse clientLogResponse = clientLogRepo.getClientLogById(clientLogId)
					.orElseThrow(()-> new ClientLogNotFoundException("Error: Unable to find the Client log with the Client_Log_Id: " + clientLogId));
		log.info("Action: ClientLog by clientLog_ID: {} fetched successfully.", clientLogId);
		return clientLogResponse;
	}

	
	
	
	@Override
	public Page<ClientLogResponse> getAllClientLog( Pageable pageable) {
		Page<ClientLogResponse> clientLogList = clientLogRepo.getAllClientLog(pageable);
		if(clientLogList.isEmpty()) {
			log.warn(" No Client_LOG's found in the Database.");
		}
		log.info("Action: Successfully Fetched {} number of Client_Log Fromt the data.", clientLogList.getNumberOfElements());		
		return clientLogList;
	}

}
