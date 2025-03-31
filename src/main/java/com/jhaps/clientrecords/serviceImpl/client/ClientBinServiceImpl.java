package com.jhaps.clientrecords.serviceImpl.client;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.response.ClientBinDto;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientBin;
import com.jhaps.clientrecords.exception.client.ClientBinNotFoundException;
import com.jhaps.clientrecords.repository.client.ClientBinRepository;
import com.jhaps.clientrecords.service.client.ClientBinService;
import com.jhaps.clientrecords.util.ClientMapper;
import com.jhaps.clientrecords.util.Mapper;
import com.jhaps.clientrecords.util.UserMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ClientBinServiceImpl implements ClientBinService{
	
	private ClientBinRepository clientBinRepo;
	private Mapper mapper;
	private ClientMapper clientMapper;
	
	
	
	@Override
	public void insertInClientBin(Client client) {
		log.info("Action: Inserting Client of ID: {} in ClientBin", client.getId());
		ClientBin clientBin = new ClientBin();
		clientBin.setClientId(client.getId());
		clientBin.setFirstName(client.getFirstName());
		clientBin.setLastName(client.getLastName());
		clientBin.setDateOfBirth(client.getDateOfBirth());
		clientBin.setPostalCode(client.getPostalCode());
		log.info("Action: Client of ID: {} inserted in ClientBin Successfully.", client.getId());
		clientBinRepo.save(clientBin);
		
	}

	@Override
	public void deleteFromClientBin(int clientBinId) {
		log.info("Deleting the Client_Contents of ClientBin_ID: {} from ClientBin", clientBinId);
		clientBinRepo.deleteById(clientBinId);
	}

	@Override
	public ClientBinDto getClientBinById(int clientBinId) {
		log.info("Action: getting ClientBin with clientBinId: {}", clientBinId);
		ClientBin clientBin = clientBinRepo.findById(clientBinId)
			.orElseThrow(()-> new ClientBinNotFoundException("Unable to find Clients in ClientBin with Client_Bin_Id: " + clientBinId));
		return clientMapper.toClientBinDto(clientBin); // converting to clientBinDto
	}

	@Override
	public Page<ClientBinDto> getAllFromClientBin(Pageable pageable) {
		Page<ClientBin> clientBinList =  clientBinRepo.findAll(pageable);
		return clientBinList.map(clientMapper::toClientBinDto);
	}

	
	
	
	
	
	
	
	
}//ends class
