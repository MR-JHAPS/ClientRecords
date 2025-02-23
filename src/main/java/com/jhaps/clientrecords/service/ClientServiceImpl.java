package com.jhaps.clientrecords.service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.exception.EntityNotFoundException;
import com.jhaps.clientrecords.exception.EntityOperationException;
import com.jhaps.clientrecords.repository.ClientRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ClientServiceImpl implements ClientService  {

	private ClientRepository clientRepo;

//CONSTRUCTOR	
	public ClientServiceImpl(ClientRepository clientRepo) {
		this.clientRepo = clientRepo;
	}
	
	
	
//METHODS
//------------------------------CRUD------------------------------------------------------------------------------------------------
	@Override
	public List<Client> findAllClients() {
			return clientRepo.findAll();
		}

	
	
	@Override
	public Optional<Client> findClientById(int id) {		
		try {
			return clientRepo.findById(id);
		}catch(DataAccessException e) {
			throw new EntityNotFoundException("client", id, e );
		}
	}
	
	
	@Override
	public void saveClient(Client client) {
		try{
			clientRepo.save(client);
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
	public void updateClientById(int id, @Valid Client clientUpdateInfo) {
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
	public List<Client> findClientBySearchQuery(String searchQuery) {
		try {
			return clientRepo.searchClients(searchQuery);
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching", "client", e);
		}
	}

	
	@Override
	public List<Client> findClientsByFirstName(String firstName) {
		try {
			return clientRepo.findByFirstName(firstName);
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By FirstName", "client", e);
		}	
	}

	
	@Override
	public List<Client> findClientsByLastName(String lastName) {
		try {
			return clientRepo.findByLastName(lastName);
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By LastName", "client", e);
		}	
	}

	
	@Override
	public List<Client> findClientsByDateOfBirth(LocalDate dateOfBirth) {
		try {
			return clientRepo.findByDateOfBirth(dateOfBirth);
		}catch (DataAccessException e) {
			throw new EntityOperationException("Searching By DateOfBirth", "client", e);
		}	
		
	}

	
	@Override
	public List<Client> findClientsByPostalCode(String postalCode) {
		try {
			return clientRepo.findByPostalCode(postalCode);
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
