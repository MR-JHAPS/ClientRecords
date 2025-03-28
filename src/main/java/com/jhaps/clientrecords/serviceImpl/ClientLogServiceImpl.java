package com.jhaps.clientrecords.serviceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.ClientLogDto;
import com.jhaps.clientrecords.entity.ClientLog;
import com.jhaps.clientrecords.enums.ModificationType;
import com.jhaps.clientrecords.exception.ClientLogNotFoundException;
import com.jhaps.clientrecords.repository.ClientLogRepository;
import com.jhaps.clientrecords.service.ClientLogService;

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
	public void insertClientLog(int userId, int clientId, ModificationType modificationType) {
		ClientLog clientLog = new ClientLog();
			clientLog.setUserId(userId);
			clientLog.setClientId(clientId);
			clientLog.setModificationType(modificationType);
			clientLogRepo.save(clientLog);
	}

	
	@Override
	public ClientLogDto getClientLogById(int id) {
		ClientLogDto clientLogDto = clientLogRepo.getUpdatedClientLogById(id)
					.orElseThrow(()-> new ClientLogNotFoundException("Error: Unable to find the Client log with the Client_Log_Id: " + id));
		return clientLogDto;
	}

	
	
	
	@Override
	public Page<ClientLogDto> getAllClientLog( Pageable pageable) {
		Page<ClientLogDto> clientLogList = clientLogRepo.getAllUpdatedClientLog(pageable);
		if(clientLogList.isEmpty()) {
			log.warn(" No Client_LOG's found in the Database.");
		}
		log.info("Action: Successfully Fetched {} number of Client_Log Fromt the data.", clientLogList.getNumberOfElements());		
		return clientLogList;
	}

}
