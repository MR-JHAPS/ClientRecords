package com.jhaps.clientrecords.serviceImpl.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jhaps.clientrecords.dto.response.ClientBinResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientBin;
import com.jhaps.clientrecords.entity.system.User;
import com.jhaps.clientrecords.exception.client.ClientBinNotFoundException;
import com.jhaps.clientrecords.exception.client.ClientNotFoundException;
import com.jhaps.clientrecords.exception.system.UserNotFoundException;
import com.jhaps.clientrecords.repository.client.ClientBinRepository;
import com.jhaps.clientrecords.repository.client.ClientRepository;
import com.jhaps.clientrecords.repository.system.UserRepository;
import com.jhaps.clientrecords.service.client.ClientBinService;
import com.jhaps.clientrecords.service.client.ClientService;
import com.jhaps.clientrecords.service.system.UserService;
import com.jhaps.clientrecords.util.mapper.ClientMapper;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ClientBinServiceImpl implements ClientBinService{
	
	private ClientBinRepository clientBinRepo;
	private ClientRepository clientRepo; //This is so that the clientBin client can be restored to client-Table/entity.
	private UserRepository userRepo;
	private ClientMapper clientMapper;
	private EntityManager entityManager;
	
	
	@Override
	public void insertInClientBin(Client client) {
		log.info("Action: Inserting Client of ID: {} in ClientBin", client.getId());
		ClientBin clientBin = new ClientBin();
		clientBin.setClientId(client.getId());
		clientBin.setFirstName(client.getFirstName());
		clientBin.setLastName(client.getLastName());
		clientBin.setDateOfBirth(client.getDateOfBirth());
		clientBin.setPostalCode(client.getPostalCode());
		clientBin.setUserId(client.getUser().getId());
		log.info("Action: Client of ID: {} inserted in ClientBin Successfully.", client.getId());
		clientBinRepo.save(clientBin);
		
	}

	@Override
	public void deleteFromClientBin(int clientBinId) {
		log.info("Deleting the Client_Contents of ClientBin_ID: {} from ClientBin", clientBinId);
		clientBinRepo.deleteById(clientBinId);
	}

	@Override
	public ClientBinResponse getClientBinById(int clientBinId) {
		log.info("Action: getting ClientBin with clientBinId: {}", clientBinId);
		ClientBin clientBin = clientBinRepo.findById(clientBinId)
			.orElseThrow(()-> new ClientBinNotFoundException("Unable to find Clients in ClientBin with Client_Bin_Id: " + clientBinId));
		return clientMapper.toClientBinDto(clientBin); // converting to clientBinDto
	}

	@Override
	public Page<ClientBinResponse> getAllFromClientBin(Pageable pageable) {
		Page<ClientBin> clientBinList =  clientBinRepo.findAll(pageable);
		return clientBinList.map(clientMapper::toClientBinDto);
	}

	
	@Transactional
	@Override
	public void restoreFromClientBin(int id) {
		ClientBin clientBin = clientBinRepo.findById(id)
						.orElseThrow(() -> new ClientBinNotFoundException("ClientBin with id: " + id + " not Found." ));
		//getting user from the userId stored in clientBin.
		User user = getUserById(clientBin.getUserId());
		/* 
		 * We cannot restore the original clientId back to clientEntity from clientBinEntity.
		 * To prevent this I am using the native query to force insert the original clientID back to clientEntity from clientBinEntity.
		 */
	    clientBinRepo.restoreClientFromClientBin(clientBin.getClientId(),
	    											clientBin.getFirstName(), 
	    											clientBin.getLastName(),
	    											clientBin.getPostalCode(),
	    											clientBin.getDateOfBirth(),
	    											user.getId());
	    clientBinRepo.delete(clientBin);
	}

	
	
	
	
	/*     Private method for getting user By id from UserRepository. */
	
	private User getUserById(int userId) {
		return userRepo.findById(userId)
				.orElseThrow(()-> new UserNotFoundException("Error: Unable to find the user with ID: " + userId));
	}
	
	
	
	
	
	
	
}//ends class
