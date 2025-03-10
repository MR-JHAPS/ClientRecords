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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.exception.ClientNotFoundException;
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
	
	private Mapper mapper; //this contains the custom mapping 

//CONSTRUCTOR	
	public ClientServiceImpl(ClientRepository clientRepo, Mapper mapper) {
		this.clientRepo = clientRepo;
		this.mapper = mapper;
	}
	
	
	
/* METHODS */
	
//------------------------------CRUD------------------------------------------------------------------------------------------------
	/* Because the return type is Page<ClientDto>
	   if we try to stream like normal :
		**** clientList.stream().map(mapper::toClientDto).collect(Collectors.toList()).
	   it converts to list but we want Page type collections not List so it won't work.
	   Page is itself a type of collections. I didn't knew that. Page, list are type of collections.
	 */
	@Override
	public Page<ClientDto> findAllClients(Pageable pageable) {
		Page<Client> clientList = clientRepo.findAll(pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("No Clients found in the Database.");
		}
		return clientList.map(mapper::toClientDto); 
		
	}
	
	
	@Override
	public ClientDto findClientById(int id) {		
		Client client = clientRepo.findById(id).orElseThrow(()->
						new ClientNotFoundException("Unable to Find the Client with id : " + id));
		return mapper.toClientDto(client);
	}
	
	
	@Override
	public void saveClient(ClientDto clientDto) {
		clientRepo.save(  mapper.toClientEntity(clientDto) );  //converting DTO to entity before saving to repository.
	}

	
	@Override
	public void deleteClientById(int id) {
		Client client = clientRepo.findById(id).orElseThrow(()->
			new ClientNotFoundException("Client with ID : " + id + " not found, to delete."));
		clientRepo.delete(client);
	}

	
	@Transactional
	@Override
	public void updateClientById(int id, ClientDto clientUpdateInfo) {
		Client client = clientRepo.findById(id).orElseThrow(
							()-> new ClientNotFoundException("Client with ID : " +id + "not found to update."));
		client.setFirstName(clientUpdateInfo.getFirstName());
		client.setLastName(clientUpdateInfo.getLastName());
		client.setDateOfBirth(clientUpdateInfo.getDateOfBirth());
		client.setPostalCode(clientUpdateInfo.getPostalCode());
		clientRepo.save(client);	
	}

//--------------------SEARCHING----------------------------------------------------------------------------------------------------------	
	@Override
	public Page<ClientDto> findClientBySearchQuery(String searchQuery, Pageable pageable) {
		Page<Client> clientList = clientRepo.searchClients(searchQuery, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client with search Query " + searchQuery + " not found.");
		}
		return clientList.map(mapper::toClientDto);
	}

	
	@Override
	public Page<ClientDto> findClientsByFirstName(String firstName, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByFirstNameStartingWithIgnoreCase(firstName, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by FirstName " + firstName + " not found.");
		}
		return clientList.map(mapper::toClientDto);
	}

	
	@Override
	public Page<ClientDto> findClientsByLastName(String lastName, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByLastNameStartingWithIgnoreCase(lastName, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by LastName " + lastName + " not found.");
		}
		return clientList.map(mapper::toClientDto); 
		/* OR using Lambda : 
		  return clientList.map(list -> mapper.toClientDto(list));
		*/
	}

	
	@Override
	public Page<ClientDto> findClientsByDateOfBirth(LocalDate dateOfBirth, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByDateOfBirth(dateOfBirth, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by DateOfBirth " + dateOfBirth + " not found.");
		}
		return clientList.map(mapper::toClientDto);
	}

	
	@Override
	public Page<ClientDto> findClientsByPostalCode(String postalCode, Pageable pageable) {
			Page<Client> clientList = clientRepo.findByPostalCodeStartingWithIgnoreCase(postalCode, pageable);
			if(clientList.isEmpty()) {
				throw new ClientNotFoundException("Client search by PostalCode " + postalCode + " not found.");
			}
			return clientList.map(mapper::toClientDto);	
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
