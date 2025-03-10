package com.jhaps.clientrecords.util;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.dto.UserDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.entity.User;

@Component
public class Mapper {

	
	//CLIENT---> CLIENT-DTO
	public  ClientDto toClientDto(Client clientEntity) {
		ClientDto dto = new ClientDto();
		dto.setId(clientEntity.getId());
		dto.setFirstName(clientEntity.getFirstName());
		dto.setLastName(clientEntity.getLastName());
		dto.setPostalCode(clientEntity.getPostalCode());
		dto.setDateOfBirth(clientEntity.getDateOfBirth());
		return dto;
	}
	
	
//	//CLIENT---> CLIENT-DTO STATIC
//		public static ClientDto toClientDtoStatic(Client clientEntity) {
//			ClientDto dto = new ClientDto();
//			dto.setId(clientEntity.getId());
//			dto.setFirstName(clientEntity.getFirstName());
//			dto.setLastName(clientEntity.getLastName());
//			dto.setPostalCode(clientEntity.getPostalCode());
//			dto.setDateOfBirth(clientEntity.getDateOfBirth());
//			return dto;
//		}
	
	
	//CLIENT-DTO ------> CLIENT
	public  Client toClientEntity(ClientDto clientDto) {
		Client client = new Client();
			client.setId(clientDto.getId());
			client.setFirstName(clientDto.getFirstName());
			client.setLastName(clientDto.getLastName());
			client.setPostalCode(clientDto.getPostalCode());
			client.setDateOfBirth(clientDto.getDateOfBirth());
		return client;
		
	}
	
	
	
	//USER ---> USER-DTO
	public UserDto toUserDto(User user) {
		UserDto dto = new UserDto();
				dto.setEmail(user.getEmail());
				dto.setPassword(user.getPassword());
		return dto;
	}
	
	
	//USER-DTO ------> USER-ENTITY
	public User toUserEntity(UserDto userDto) {
		User user = new User();
			 user.setEmail(userDto.getEmail());
			 user.setPassword(userDto.getPassword());
		return user;	 
	}
	
	
	
	
	
	
	
	
	
	
	
}//ends class
