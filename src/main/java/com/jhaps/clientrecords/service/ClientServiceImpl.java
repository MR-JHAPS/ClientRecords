package com.jhaps.clientrecords.service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.exceptionHandler.EntityNotFoundException;
import com.jhaps.clientrecords.exceptionHandler.EntityOperationException;
import com.jhaps.clientrecords.repository.ClientRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientServiceImpl implements ClientService  {

	private ClientRepository clientRepo;

//CONSTRUCTOR	
	public ClientServiceImpl(ClientRepository clientRepo) {
		this.clientRepo = clientRepo;
	}
	
	
	
//METHODS
	@Override
	public List<Client> findAllClients() {
		return clientRepo.findAll();
	}


	
	@Override
	public Optional<Client> findClientById(int id) {
		//Here I need to implement logger
		
		return clientRepo.findById(id);
	}

	@Override
	public List<Client> findClientsByFirstName(String firstName) {
		return clientRepo.findByFirstName(firstName);
	}

	@Override
	public List<Client> findClientsByLastName(String lastName) {
		return clientRepo.findByLastName(lastName);
	}

	@Override
	public List<Client> findClientsByDateOfBirth(LocalDate dateOfBirth) {
		return clientRepo.findByDateOfBirth(dateOfBirth);
	}

	@Override
	public List<Client> findClientsByPostalCode(String postalCode) {
		return clientRepo.findByPostalCode(postalCode);
	}

	@Override
	public void saveClient(Client client) {
		try{
			clientRepo.save(client);
			
		}catch(IllegalArgumentException e) { //this catches only database related exceptions.
			//EntityOperationException is a manually created ExceptionHandler.
			throw new EntityOperationException("save", "client", e);				
		}
		
	}

	@Override
	public void deleteClientById(int id) {
		
		try {
			clientRepo.deleteById(id);
		}catch(IllegalArgumentException e) {
			throw new EntityOperationException("Delete", "Client", e);
		}
		
	}

	@Transactional
	@Override
	public void updateClientById(int id, Client clientUpdateInfo) {
		try {
			Client client = clientRepo.findById(id).orElseThrow(
								()-> new EntityNotFoundException("Client", id));
			
			client.setFirstName(clientUpdateInfo.getFirstName());
			client.setLastName(clientUpdateInfo.getLastName());
			client.setDateOfBirth(clientUpdateInfo.getDateOfBirth());
			client.setPostalCode(clientUpdateInfo.getPostalCode());
			clientRepo.save(client);
			
		}catch(Exception e) {
			throw new EntityOperationException("Update", "Client", e);
		}
		
	}



	@Override
	public List<Client> sortClientByFirstNameAscending(List<Client> clientList) {
//		clientList.sort((user1, user2)->(user1.getFirstName()).compareToIgnoreCase(user2.getFirstName()));
//		return clientList;
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





	




////@Override
////public List<Client> findAllClientsAsc() {
////	List<Client> clientList = clientRepo.findAll();
////	clientList.sort((user1, user2)->(user1.getFirstName()).compareToIgnoreCase(user2.getFirstName()));
////	return clientList;
////
////}
//
//
//public List<Client> findAllClientsAsc() {
//	List<Client> clientList = clientRepo.findAll();
//	sortClientByFirstNameAsc(clientList);
//	return clientList;
//
//}	
//
//
////THIS METHOD IS FOR HANDLING SORTING BY FIRSTNAME
//public List<Client> sortClientByFirstNameAsc(List<Client> clientList){
//	clientList.sort((user1, user2)->(user1.getFirstName()).compareToIgnoreCase(user2.getFirstName()));
//	return clientList;
//}




//@Override
//public List<Client> findAllClientsAsc() {
//	List<Client> clientList = clientRepo.findAll();
//	return sortByFirstName(clientList);
//	
//}
//
//public List<Client> sortByFirstName(List<Client> clientList){
//	
//	return clientList.sort((user1, user2)->(user1.getFirstName().compareToIgnoreCase(user2.getFirstName())));
//}
	
	
	
	
	
	
	
	
	
	
}//ends class
