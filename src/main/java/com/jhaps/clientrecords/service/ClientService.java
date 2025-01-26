package com.jhaps.clientrecords.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.jhaps.clientrecords.entity.Client;

public interface ClientService {

	public List<Client> findAllClients();
	
	public Optional<Client> findClientById(int id);
	
	public List<Client> findClientsByFirstName(String firstName);
	
	public List<Client> findClientsByLastName(String lastName);
	
	public List<Client> findClientsByDateOfBirth(LocalDate dateOfBirth);
	
	public List<Client> findClientsByPostalCode(String postalCode);
	
	public void saveClient(Client client);
	
	public void deleteClientById(int id);
	
	public void updateClientById(int id);
	
	
}
