package com.jhaps.clientrecords.serviceImpl;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.entity.User;
import com.jhaps.clientrecords.enums.ModificationType;
import com.jhaps.clientrecords.exception.ClientNotFoundException;
import com.jhaps.clientrecords.repository.ClientRepository;
import com.jhaps.clientrecords.service.ClientBinService;
import com.jhaps.clientrecords.service.ClientLogService;
import com.jhaps.clientrecords.service.ClientService;
import com.jhaps.clientrecords.util.Mapper;
import com.jhaps.clientrecords.util.SecurityUtils;

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
	
	private Mapper mapper; //this contains the custom mapping 
	
	private ClientLogService clientLogService;
	
	private ClientBinService clientBinService;



	
	
	
	
// METHODS 
/*	------------------------------CRUD------------------------------------------------------------------------------------------------*/	
	
	/* Because the return type is Page<ClientDto>
	   if we try to stream like normal :
		**** clientList.stream().map(mapper::toClientDto).collect(Collectors.toList()).
	   it converts to list but we want Page type collections not List so it won't work.
	   Page is itself a type of collections. I didn't knew that. Page, list are type of collections.
	 */
	@Override
	public Page<ClientDto> findAllClients(Pageable pageable) {		
		log.info("Fetching clients with Pagination - page{} , size{}",pageable.getPageNumber(),pageable.getPageSize());
		Page<Client> clientList = clientRepo.findAll(pageable);
		if(clientList.isEmpty()) {
			log.warn("No clients found for the given page request");
			throw new ClientNotFoundException("No Clients found in the Database.");
		}
		log.info("Successfully fetched {} clients", clientList.getNumberOfElements());
		return clientList.map(mapper::toClientDto); 
		
	}
	
	
	@Override
	public ClientDto findClientById(int id) {		
		Client client = clientRepo.findById(id).orElseThrow(()->
						new ClientNotFoundException("Unable to Find the Client with id : " + id));
		log.info("Client by id :{} found Successfully.", id);
		return mapper.toClientDto(client);
	}
	
	
	@Override
	public void saveClient(ClientDto clientDto) {
		User currentUser = SecurityUtils.getCurrentUser();
		log.info("Saving Client with name {} .",clientDto.getFirstName());
		Client savedClient = clientRepo.save(mapper.toClientEntity(clientDto));  //converting DTO to entity before saving to repository.
		log.info("Client with name {} saved Successfully.",clientDto.getFirstName());
		//logging the client in the clientLog.
		clientLogService.insertInClientLog(currentUser, savedClient, ModificationType.INSERT);
	}

	
	@Override
	public void deleteClientById(int clientId) {
		log.warn("Action: Deleting the Client with ClientID: {}", clientId);
		User currentUser = SecurityUtils.getCurrentUser();
		Client client = clientRepo.findById(clientId).orElseThrow(()->
			new ClientNotFoundException("Client with ID : " + clientId + " not found, to delete."));	
		clientBinService.insertInClientBin(client); //inserting client to client been before deleting from the client.
		clientLogService.insertInClientLog(currentUser, client, ModificationType.DELETE); // inserting client in client log before deleting.
		clientRepo.delete(client);
		log.info("Client with id {} deleted Successfully.",clientId);

	}

	
	@Transactional
	@Override
	public void updateClientById(int id, ClientDto clientUpdateInfo) {
		User currentUser = SecurityUtils.getCurrentUser();
		Client client = clientRepo.findById(id).orElseThrow(
							()-> new ClientNotFoundException("Client with ID : " +id + "not found to update."));
		client.setFirstName(clientUpdateInfo.getFirstName());
		client.setLastName(clientUpdateInfo.getLastName());
		client.setDateOfBirth(clientUpdateInfo.getDateOfBirth());
		client.setPostalCode(clientUpdateInfo.getPostalCode());
		clientRepo.save(client);	
		log.info("Client with id :{} updated with new info",id);
		
		clientLogService.insertInClientLog(currentUser, client, ModificationType.UPDATE);
	}

//--------------------SEARCHING----------------------------------------------------------------------------------------------------------	
	@Override
	public Page<ClientDto> findClientBySearchQuery(String searchQuery, Pageable pageable) {
		Page<Client> clientList = clientRepo.searchClients(searchQuery, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client with search Query " + searchQuery + " not found.");
		}
		log.info("Finding Client By Search Query :{} is Executed Successfully. and fetched :{} clients", searchQuery, clientList.getNumberOfElements());
		return clientList.map(mapper::toClientDto);
	}

	
	@Override
	public Page<ClientDto> findClientsByFirstName(String firstName, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByFirstNameStartingWithIgnoreCase(firstName, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by FirstName " + firstName + " not found.");
		}
		log.info("Finding Client By FirstName :{} is Executed Successfully. and fetched :{} clients", firstName, clientList.getNumberOfElements());
		return clientList.map(mapper::toClientDto);
	}

	
	@Override
	public Page<ClientDto> findClientsByLastName(String lastName, Pageable pageable) {
		Page<Client> clientList = clientRepo.findByLastNameStartingWithIgnoreCase(lastName, pageable);
		if(clientList.isEmpty()) {
			throw new ClientNotFoundException("Client search by LastName " + lastName + " not found.");
		}
		log.info("Finding Client By LastName :{} is Executed Successfully. and fetched :{} clients", lastName, clientList.getNumberOfElements());
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
		log.info("Finding Client By DateOfBirth :{} is Executed Successfully. and fetched :{} clients", dateOfBirth, clientList.getNumberOfElements());
		return clientList.map(mapper::toClientDto);
	}

	
	@Override
	public Page<ClientDto> findClientsByPostalCode(String postalCode, Pageable pageable) {
			Page<Client> clientList = clientRepo.findByPostalCodeStartingWithIgnoreCase(postalCode, pageable);
			if(clientList.isEmpty()) {
				throw new ClientNotFoundException("Client search by PostalCode " + postalCode + " not found.");
			}
			log.info("Finding Client By PostalCode :{} is Executed Successfully. and fetched :{} clients", postalCode, clientList.getNumberOfElements());
			return clientList.map(mapper::toClientDto);	
	}
	
	
	
}//ends class
