package com.jhaps.clientrecords.service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;

public interface ClientService {

	//CRUD OPERATIONS
	public Page<ClientDto> findAllClients(Pageable pageable);
	
	public Optional<ClientDto> findClientById(int id);

	public void saveClient(ClientDto clientDto);
	
	public void deleteClientById(int id);
	
	public void updateClientById(int id, ClientDto clientUpdateInfo);
	
	//THIS IS FOR THE SEARCH QUERY 
	public Page<ClientDto> findClientBySearchQuery(String searchQuery, Pageable pageable);
	
	public Page<ClientDto> findClientsByFirstName(String firstName, Pageable pageable);
	
	public Page<ClientDto> findClientsByLastName(String lastName, Pageable pageable);
	
	public Page<ClientDto> findClientsByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);
	
	public Page<ClientDto> findClientsByPostalCode(String postalCode, Pageable pageable);
	
	
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
