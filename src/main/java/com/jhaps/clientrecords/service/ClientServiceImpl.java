package com.jhaps.clientrecords.service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService  {

	@Autowired
	private ClientRepository clientRepo;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteClientById(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateClientById(int id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
}//ends class
