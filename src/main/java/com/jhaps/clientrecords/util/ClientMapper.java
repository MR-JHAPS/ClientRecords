package com.jhaps.clientrecords.util;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.ClientRequest;
import com.jhaps.clientrecords.dto.response.ClientBinResponse;
import com.jhaps.clientrecords.dto.response.ClientResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientBin;

@Component
public class ClientMapper {

	
	
	
	/* To_ClientResponse */
		public  ClientResponse toClientResponse(Client clientEntity) {
			ClientResponse dto = new ClientResponse();
			dto.setId(clientEntity.getId());
			dto.setFirstName(clientEntity.getFirstName());
			dto.setLastName(clientEntity.getLastName());
			dto.setPostalCode(clientEntity.getPostalCode());
			dto.setDateOfBirth(clientEntity.getDateOfBirth());
			return dto;
		}
		
		
	/* To_ClientEntity*/
		public  Client toClientEntity(ClientRequest request) {
			Client client = new Client();
			client.setId(request.getId());
			client.setFirstName(request.getFirstName());
			client.setLastName(request.getLastName());
			client.setPostalCode(request.getPostalCode());
			client.setDateOfBirth(request.getDateOfBirth());
			return client;
		}
	
		
		
		
		
/*-----------------------ClientBin Conversion ---------------------------------------------------------------------------------------------------------------*/	
		
		public ClientBinResponse toClientBinDto(ClientBin clientBin) {
			ClientBinResponse dto = new ClientBinResponse();
			dto.setId(clientBin.getId());
			dto.setClientId(clientBin.getClientId());
			dto.setFirstName(clientBin.getFirstName());
			dto.setLastName(clientBin.getLastName());
			dto.setDateOfBirth(clientBin.getDateOfBirth());
			dto.setPostalCode(clientBin.getPostalCode());
			return dto;
		}
		
		
		
		public ClientBin toClientBinEntity(ClientBinResponse clientBinResponse) {
			ClientBin entity = new ClientBin();
			entity.setClientId(clientBinResponse.getClientId());
			entity.setFirstName(clientBinResponse.getFirstName());
			entity.setLastName(clientBinResponse.getLastName());
			entity.setDateOfBirth(clientBinResponse.getDateOfBirth());
			entity.setPostalCode(clientBinResponse.getPostalCode());
			return entity;
		}
	
	
		
		// This is to restore the clientBin client to Client-Entity.
		public Client toClient (ClientBin clientBin) {
			Client client = new Client();
			client.setId(clientBin.getClientId());
			client.setFirstName(clientBin.getFirstName());
			client.setLastName(clientBin.getLastName());
			client.setDateOfBirth(clientBin.getDateOfBirth());
			client.setPostalCode(clientBin.getPostalCode());
			return client;
		}
		
		
		
		
		
		
}
