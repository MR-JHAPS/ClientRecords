package com.jhaps.clientrecords.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.jhaps.clientrecords.entity.Client;

public interface ClientService {

	//CRUD OPERATIONS
	public List<Client> findAllClients();
	
	public Optional<Client> findClientById(int id);

	public void saveClient(Client client);
	
	public void deleteClientById(int id);
	
	public void updateClientById(int id, Client clientUpdateInfo);
	
	//THIS IS FOR THE SEARCH QUERY 
	public List<Client> findClientBySearchQuery(String searchQuery);
	
	public List<Client> findClientsByFirstName(String firstName);
	
	public List<Client> findClientsByLastName(String lastName);
	
	public List<Client> findClientsByDateOfBirth(LocalDate dateOfBirth);
	
	public List<Client> findClientsByPostalCode(String postalCode);
	
	
	//THIS IS FOR SORTING CLIENTS
	public List<Client> sortClientByFirstNameAscending(List<Client> clientList);
	
	public List<Client> sortClientByFirstNameDescending(List<Client> clientList);
	
	public List<Client> sortClientByLastNameAscending(List<Client> clientList);
	
	public List<Client> sortClientByLastNameDescending(List<Client> clientList);
	
	public List<Client> sortClientByDateOfBirthAscending(List<Client> clientList);
	
	public List<Client> sortClientByDateOfBirthDescending(List<Client> clientList);
	
	public List<Client> sortClientByPostalCodeAscending(List<Client> clientList);
	
	public List<Client> sortClientByPostalCodeDescending(List<Client> clientList);
	
	
	
	
}
