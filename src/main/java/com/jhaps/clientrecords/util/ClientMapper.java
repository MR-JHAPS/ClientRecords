package com.jhaps.clientrecords.util;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.ClientDto;
import com.jhaps.clientrecords.dto.request.ClientRequest;
import com.jhaps.clientrecords.dto.response.ClientBinDto;
import com.jhaps.clientrecords.dto.response.ClientResponse;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientBin;

@Component
public class ClientMapper {

	public ClientBinDto toClientBinDto(ClientBin clientBin) {
		ClientBinDto dto = new ClientBinDto();
		dto.setId(clientBin.getId());
		dto.setClientId(clientBin.getClientId());
		dto.setFirstName(clientBin.getFirstName());
		dto.setLastName(clientBin.getLastName());
		dto.setDateOfBirth(clientBin.getDateOfBirth());
		dto.setPostalCode(clientBin.getPostalCode());
		return dto;
	}
	
	
	//CLIENT---> CLIENT-RESPONSE
		public  ClientResponse toClientResponse(Client clientEntity) {
			ClientResponse dto = new ClientResponse();
			dto.setId(clientEntity.getId());
			dto.setFirstName(clientEntity.getFirstName());
			dto.setLastName(clientEntity.getLastName());
			dto.setPostalCode(clientEntity.getPostalCode());
			dto.setDateOfBirth(clientEntity.getDateOfBirth());
			return dto;
		}
		
		
		//CLIENT-REQUEST ------> CLIENT
		public  Client toClientEntity(ClientRequest dto) {
			Client client = new Client();
			client.setId(dto.getId());
			client.setFirstName(dto.getFirstName());
			client.setLastName(dto.getLastName());
			client.setPostalCode(dto.getPostalCode());
			client.setDateOfBirth(dto.getDateOfBirth());
			return client;
		}
	
	
	
	
}
