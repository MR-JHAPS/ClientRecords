package com.jhaps.clientrecords.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jhaps.clientrecords.dto.ClientBinDto;
import com.jhaps.clientrecords.entity.Client;

public interface ClientBinService {

	/*
	 * @Action: Inserting all the Client-Data including client_id in the ClientBin.
	 * 
	 * @Returns: void.
	 * 
	 * @Id/Primary key of ClientBin is Auto-Generated. 
	 */
	void insertInClientBin(Client client);
	
	
	/*
	 * @Param : clientBinId(i.e : primary key of the ClientBin entity)
	 */
	void deleteFromClientBin(int ClientBinId);
	
	
	/* 
	 * @Action : Getting the clientBin(i.e: Client-info) data saved inside the clientBin entity by clientBin id.
	 * 
	 * @return : ClientBinDto type .
	 * 
	 * @Param : ClientBinId
	 * 
	 * @Suggested: look the fields of "ClientBin" and "Client" entity to resolve confusion.
	 * 
	 * @important: this is clientBin_id not client_id.
	 * 			because clientBin contains its primary key("clientBin_id") and the id of the client("client_id").
	 */
	ClientBinDto getClientBinById(int clientBinId);		
	
	
	
	 /* 
	  * @Action: Getting all the client list saved in client-Bin
	  * 
	  * @Param : Pageable (contains the pagination Data)
	  * 
	  * @return : Page<ClientBinDto>. 
	  */
	Page<ClientBinDto> getAllClientBinClients(Pageable pageable);
	
	
}
