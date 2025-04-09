package com.jhaps.clientrecords.serviceImpl.client;
import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.request.ClientRequest;
import com.jhaps.clientrecords.dto.response.ClientResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.enums.ModificationType;
import com.jhaps.clientrecords.exception.client.ClientNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.client.ClientRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.service.client.ClientBinService;
import com.jhaps.clientrecords.service.client.ClientLogService;
import com.jhaps.clientrecords.service.client.ClientService;
import com.jhaps.clientrecords.util.mapper.ClientMapper;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Logging for exceptions is done in GlobalExceptionHandler.
 * */


@Service
@Slf4j
@AllArgsConstructor //	lombok - for constructor. 
public class ClientServiceImpl implements ClientService  {

	private ClientRepository clientRepo;
	private ClientLogService clientLogService;
	private ClientBinService clientBinService;	
//	private UserService userService;
	private ClientMapper clientMapper;
	private EntityManager entityManager;
	private UserRepository userRepo; // using repository to prevent circular dependency.
	


/*	------------------------------CRUD------------------------------------------------------------------------------------------------*/	
	
	
	
	
	@Override
	public Page<ClientResponse> findAllClients(Pageable pageable) {		
		log.info("Fetching clients with Pagination - page{} , size{}",pageable.getPageNumber(),pageable.getPageSize());
		Page<Client> clientList = clientRepo.findAll(pageable);
		if(clientList.isEmpty()) {
			log.warn("No clients found for the given page request");
			throw new ClientNotFoundException("No Clients found in the Database.");
		}
		log.info("Successfully fetched {} clients", clientList.getNumberOfElements());
		return clientList.map(clientMapper::toClientResponse); 
	}
	
	
	@Override
	public ClientResponse findClientById(int id) {	
		log.info("Action: Attempting to get a client by id: {}", id);
		Client client = clientRepo.findById(id).orElseThrow(()->
						new ClientNotFoundException("Unable to Find the Client with id : " + id));
		log.info("Client by id :{} found Successfully.", id);
		return clientMapper.toClientResponse(client);
	}
	
	
	@Override
	public void saveClient(String userEmail, ClientRequest clientRequest) {
		User currentUser = userRepo.findUserByEmail(userEmail).orElseThrow(()-> new UserNotFoundException("ClientService: user not found: " + userEmail));
		log.info("Saving Client with name {} .",clientRequest.getFirstName());
		Client client = clientMapper.toClientEntity(clientRequest);
		client.setUser(currentUser);
		
		Client savedClient = clientRepo.save(client);  //converting DTO to entity before saving to repository.
		log.info("Client with name {} saved Successfully.",clientRequest.getFirstName());
		//logging the client in the clientLog.
		clientLogService.insertInClientLog(userEmail, savedClient, ModificationType.INSERT);
	}

	
	@Override
	public void deleteClientById(String userEmail, int clientId) {
		log.warn("Action: Deleting the Client with ClientID: {}, user: {}", clientId, userEmail);
		Client client = clientRepo.findById(clientId).orElseThrow(()->
			new ClientNotFoundException("Client with ID : " + clientId + " not found, to delete."));	
		clientBinService.insertInClientBin(client); //inserting client to client bin before deleting from the client.
		clientLogService.insertInClientLog(userEmail, client, ModificationType.DELETE); // inserting client in client log before deleting.
		clientRepo.delete(client);
		log.info("Client with id {} deleted Successfully.",clientId);
	}

	
	@Transactional
	@Override
	public void updateClientById(String userEmail, int id, ClientRequest clientRequest) {
		log.info("Action: Attempting to update a client: {}, by user: {}", id, userEmail);
		Client client = clientRepo.findById(id).orElseThrow(
							()-> new ClientNotFoundException("Client with ID : " +id + "not found to update."));
		client.setFirstName(clientRequest.getFirstName());
		client.setLastName(clientRequest.getLastName());
		client.setDateOfBirth(clientRequest.getDateOfBirth());
		client.setPostalCode(clientRequest.getPostalCode());
		clientRepo.save(client);	
		log.info("Client with id :{} updated with new info",id);
		clientLogService.insertInClientLog(userEmail, client, ModificationType.UPDATE);
	}

//--------------------SEARCHING----------------------------------------------------------------------------------------------------------	
	@Override
	public Page<ClientResponse> findClientBySearchQuery(String searchQuery, Pageable pageable) {
		Page<Client> clientList = clientRepo.searchClients(searchQuery, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client with search Query " + searchQuery + " not found.");
		}
		log.info("Finding Client By Search Query :{} is Executed Successfully. and fetched :{} clients", searchQuery, clientList.getNumberOfElements());
		return clientList.map(clientMapper::toClientResponse);
	}

	
	@Override
	public Page<ClientResponse> findClientsByFirstName(String firstName, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByFirstNameStartingWithIgnoreCase(firstName, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by FirstName " + firstName + " not found.");
		}
		log.info("Finding Client By FirstName :{} is Executed Successfully. and fetched :{} clients", firstName, clientList.getNumberOfElements());
		return clientList.map(clientMapper::toClientResponse);
	}

	
	@Override
	public Page<ClientResponse> findClientsByLastName(String lastName, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByLastNameStartingWithIgnoreCase(lastName, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by LastName " + lastName + " not found.");
		}
		log.info("Finding Client By LastName :{} is Executed Successfully. and fetched :{} clients", lastName, clientList.getNumberOfElements());
		return clientList.map(clientMapper::toClientResponse); 
		/* OR using Lambda : 
		  return clientList.map(list -> mapper.toClientDto(list));
		*/
	}

	
	@Override
	public Page<ClientResponse> findClientsByDateOfBirth(LocalDate dateOfBirth, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByDateOfBirth(dateOfBirth, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by DateOfBirth " + dateOfBirth + " not found.");
		}
		log.info("Finding Client By DateOfBirth :{} is Executed Successfully. and fetched :{} clients", dateOfBirth, clientList.getNumberOfElements());
		return clientList.map(clientMapper::toClientResponse);
	}

	
	@Override
	public Page<ClientResponse> findClientsByPostalCode(String postalCode, Pageable pageable) {
			Page<Client> clientList = clientRepo.findByPostalCodeStartingWithIgnoreCase(postalCode, pageable);
			if(clientList.isEmpty()) {
				throw new ClientNotFoundException("Client search by PostalCode " + postalCode + " not found.");
			}
			log.info("Finding Client By PostalCode :{} is Executed Successfully. and fetched :{} clients", postalCode, clientList.getNumberOfElements());
			return clientList.map(clientMapper::toClientResponse);	
	}


	
	
	@Override
	public void reassignClientToAdmin(int userId) {
		String adminEmail = "admin@gmail.com";
		User user = findUserById(userId); /* User : user whose clients are to be transferred/reassigned. */
		User admin = findUserByEmail(adminEmail); /* Admin : selected user clients will be transferred/reassigned to admin. */
		List<Client> clientListOfUser = clientRepo.findByUser_Id(userId);
		if(clientListOfUser.isEmpty()) {
			log.info("Info: No clients was assigned to admin as it was not found in Database for the userId :{}.", userId);
			return;
		}
		int updated = clientRepo.bulkReassignClientsById(user.getId(), admin.getId());
		log.info("Action: {} clients were assigned from user: {} to admin: {}", updated, user.getEmail(), admin.getEmail());		
		
		/* Inserting each clientList into the clientLog because we are modifying the user of the client list: Reassignment*/
		clientListOfUser
			.forEach( client ->
						clientLogService.insertInClientLog(user.getEmail(), client, ModificationType.REASSIGNMENT)
					);		
	}
	
	
	
	@Transactional
	@Override
	public void reassignClientToAdmin(String userEmail) {
		log.info("Action: Preparing to reassign Client from user : {} to admin.", userEmail);
		String adminEmail = "admin@gmail.com";
		User user = findUserByEmail(userEmail);/* User : user whose clients are to be transferred/reassigned. */
		User admin = findUserByEmail(adminEmail);/* Admin : selected user clients will be transferred/reassigned to admin. */
		
		List<Client> clientListOfUser = clientRepo.findByUser_Email(userEmail);
		/* Checking if the clientList is empty. */
		if(clientListOfUser.isEmpty()) {
			log.info("Info: No clients was assigned to admin as it was not found in Database for the userEmail: {}.", userEmail);
			return;
		}
		
		/* Logging the client.*/
		clientListOfUser
			.forEach( client ->
						clientLogService.insertInClientLog(user.getEmail(), client, ModificationType.REASSIGNMENT)
					);		
		
		try {
			//Reassigning the clients from user to admin.
			int updated = clientRepo.bulkReassignClientsById(user.getId(), admin.getId());
			log.info("Action: {} clients were assigned from user: {} to admin: {}", updated, user.getEmail(), admin.getEmail());		
		}catch(Exception e ) {
			log.warn("Error occured while reassigning client to admin from user: {}", user.getEmail());
			throw new RuntimeException(" Error while reassigning the client to admin from user : " + user.getEmail());
		}
		
	}
	
	
	
	
	
	/* These are private methods that gets user from userRepository so that we do not need to be dependent on userService 
	 * Using UserService will cause the circular Dependency issues.
	 */
	
	private User findUserByEmail(String userEmail) {
		return userRepo.findByEmail(userEmail)
				.orElseThrow(()->  new UserNotFoundException("ClientServiceImpl: User with email: " + userEmail + " not found"));	
	}
	
	private User findUserById(int userID) {
		return userRepo.findById(userID)
				.orElseThrow(()->  new UserNotFoundException("ClientServiceImpl: User with Id: " + userID + " not found"));	
	}
	
	
	
}//ends class
