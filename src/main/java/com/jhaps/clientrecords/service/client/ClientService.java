package com.jhaps.clientrecords.service.client;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.request.ClientRequest;
import com.jhaps.clientrecords.dto.response.ClientResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.system.User;

public interface ClientService {

	//CRUD OPERATIONS
	public Page<ClientResponse> findAllClients(Pageable pageable);
	
	public ClientResponse findClientById(int id);

	public void saveClient(String userEmail, ClientRequest clientRequest); /* @param userEmail is to save the user in the clientLog*/
	
	public void deleteClientById(String userEmail, int id); /*  @param userEmail to log the user in clientLog */
	
	public void updateClientById(String userEmail, int id, ClientRequest clientUpdateInfo ); /*  @param userEmail to log the user in clientLog */
	
	//THIS IS FOR THE SEARCH QUERY 

	public Page<ClientResponse> findClientBySearchQuery(String searchQuery, Pageable pageable);
	
	public Page<ClientResponse> findClientsByFirstName(String firstName, Pageable pageable);
	
	public Page<ClientResponse> findClientsByLastName(String lastName, Pageable pageable);
	
	public Page<ClientResponse> findClientsByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);
	
	public Page<ClientResponse> findClientsByPostalCode(String postalCode, Pageable pageable);
	
	/* 
	 * @param userId : is the user whose clients are to be assigned to the admin.
	 * Assign all the clients of 'user' param to the 'admin'.
	 */
	public void reassignClientToAdmin(int userId);
	/* @param userEmail : is the user whose clients are to be assigned to the admin. */
	public void reassignClientToAdmin(String userEmail);
	
}
