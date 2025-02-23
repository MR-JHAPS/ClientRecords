package com.jhaps.clientrecords.service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.exception.EntityNotFoundException;
import com.jhaps.clientrecords.exception.EntityOperationException;
import com.jhaps.clientrecords.repository.ClientRepository;
import com.jhaps.clientrecords.util.ClientMapper;
import com.jhaps.clientrecords.util.Mapper;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ClientServiceImpl implements ClientService  {

	private ClientRepository clientRepo;
	
	private Mapper mapper;

//CONSTRUCTOR	
	
	public ClientServiceImpl(ClientRepository clientRepo, Mapper mapper) {
		this.clientRepo = clientRepo;
		this.mapper = mapper;
	}
	
	
	
//METHODS
//------------------------------CRUD------------------------------------------------------------------------------------------------
	@Override
	public List<ClientDto> findAllClients() {
		List<Client> clientList = clientRepo.findAll();
		return clientList.stream()
					.map(mapper::toClientDto)
					.collect(Collectors.toList());
	}

	
	
	@Override
	public Optional<ClientDto> findClientById(int id) {		
		try {
			return clientRepo.findById(id).map(mapper::toClientDto);
		}catch(DataAccessException e) {
			throw new EntityNotFoundException("client", id, e );
		}
	}
	
	
	@Override
	public void saveClient(ClientDto clientDto) {
		try{
			clientRepo.save(mapper.toClientEntity(clientDto));
		}catch(DataAccessException e) { 
			throw new EntityOperationException("save", "client", e);				
		}
	}

	
	@Override
	public void deleteClientById(int id) {
		try {
			clientRepo.deleteById(id);
		}catch(DataAccessException e) {
			throw new EntityOperationException("Delete", "Client", e);
		}
	}

	
	@Transactional
	@Override
	public void updateClientById(int id, @Valid ClientDto clientUpdateInfo) {
		try {
			Client client = clientRepo.findById(id).orElseThrow(
								()-> new EntityNotFoundException("Client", id));
			client.setFirstName(clientUpdateInfo.getFirstName());
			client.setLastName(clientUpdateInfo.getLastName());
			client.setDateOfBirth(clientUpdateInfo.getDateOfBirth());
			client.setPostalCode(clientUpdateInfo.getPostalCode());
			clientRepo.save(client);	
		}catch(DataAccessException e) {
			throw new EntityOperationException("Update", "Client", e);
		}
	}

//--------------------SEARCHING----------------------------------------------------------------------------------------------------------	
	@Override
	public List<ClientDto> findClientBySearchQuery(String searchQuery) {
		try {
			return clientRepo.searchClients(searchQuery)
							.stream()
							.map(mapper::toClientDto)
							.collect(Collectors.toList());
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching", "client", e);
		}
	}

	
	@Override
	public List<ClientDto> findClientsByFirstName(String firstName) {
		try {
			return clientRepo.findByFirstName(firstName)
					.stream()
					.map(mapper::toClientDto)
					.collect(Collectors.toList());
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By FirstName", "client", e);
		}	
	}

	
	@Override
	public List<ClientDto> findClientsByLastName(String lastName) {
		try {
			return clientRepo.findByLastName(lastName)
					.stream()
					.map(mapper::toClientDto)
					.collect(Collectors.toList());
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By LastName", "client", e);
		}	
	}

	
	@Override
	public List<ClientDto> findClientsByDateOfBirth(LocalDate dateOfBirth) {
		try {
			return clientRepo.findByDateOfBirth(dateOfBirth)
					.stream()
					.map(mapper::toClientDto)
					.collect(Collectors.toList());
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By DateOfBirth", "client", e);
		}	
		
	}

	
	@Override
	public List<ClientDto> findClientsByPostalCode(String postalCode) {
		try {
			return clientRepo.findByPostalCode(postalCode)
					.stream()
					.map(mapper::toClientDto)
					.collect(Collectors.toList());
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By PostalCode", "client", e);
		}	
	}

	
//-------------------------------SORTING-----------------------------------------------------------------------------------------------	

	@Override
	public List<Client> sortClientByFirstNameAscending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getFirstName, String::compareToIgnoreCase));
		return clientList;
	}


	@Override
	public List<Client> sortClientByFirstNameDescending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getFirstName, String::compareToIgnoreCase).reversed());
		return clientList;
	}


	@Override
	public List<Client> sortClientByLastNameAscending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getLastName, String::compareToIgnoreCase));
		return clientList;
	}


	@Override
	public List<Client> sortClientByLastNameDescending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getLastName, String::compareToIgnoreCase).reversed());
		return clientList;
	}


	@Override
	public List<Client> sortClientByDateOfBirthAscending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getDateOfBirth));
		return clientList;
	}


	@Override
	public List<Client> sortClientByDateOfBirthDescending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getDateOfBirth).reversed());
		return clientList;
	}


	@Override
	public List<Client> sortClientByPostalCodeAscending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getPostalCode, String::compareToIgnoreCase));
		return clientList;
	}


	@Override
	public List<Client> sortClientByPostalCodeDescending(List<Client> clientList) {
		clientList.sort(Comparator.comparing(Client::getPostalCode, String::compareToIgnoreCase).reversed());
		return clientList;
	}


	
	
	
	
	
}//ends class
