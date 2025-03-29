package com.jhaps.clientrecords.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.ClientLogDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.entity.ClientLog;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.enums.ModificationType;
import com.jhaps.clientrecords.exception.ClientLogNotFoundException;
import com.jhaps.clientrecords.repository.ClientLogRepository;
import com.jhaps.clientrecords.service.ClientLogService;
import com.jhaps.clientrecords.util.SecurityUtils;

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
		clientLog.setUser(SecurityUtils.getCustomUserDetailsFromSecurityContext().getUser());
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
	public ClientLogDto getClientLogById(int clientLogId) {
		log.info("Action: Getting clientLog by clientLog_ID: {}.", clientLogId);
		ClientLogDto clientLogDto = clientLogRepo.getClientLogById(clientLogId)
					.orElseThrow(()-> new ClientLogNotFoundException("Error: Unable to find the Client log with the Client_Log_Id: " + clientLogId));
		log.info("Action: ClientLog by clientLog_ID: {} fetched successfully.", clientLogId);
		return clientLogDto;
	}

	
	
	
	@Override
	public Page<ClientLogDto> getAllClientLog( Pageable pageable) {
		Page<ClientLogDto> clientLogList = clientLogRepo.getAllClientLog(pageable);
		if(clientLogList.isEmpty()) {
			log.warn(" No Client_LOG's found in the Database.");
		}
		log.info("Action: Successfully Fetched {} number of Client_Log Fromt the data.", clientLogList.getNumberOfElements());		
		return clientLogList;
	}

}
