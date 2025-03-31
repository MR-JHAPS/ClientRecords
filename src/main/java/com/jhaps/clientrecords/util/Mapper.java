package com.jhaps.clientrecords.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.dto.request.ClientDto;
import com.jhaps.clientrecords.dto.response.ClientBinDto;
import com.jhaps.clientrecords.dto.response.ImageDto;
import com.jhaps.clientrecords.dto.response.RoleDto;
import com.jhaps.clientrecords.dto.response.UserDto;
import com.jhaps.clientrecords.entity.client.Client;
import com.jhaps.clientrecords.entity.client.ClientBin;
import com.jhaps.clientrecords.entity.system.Image;
import com.jhaps.clientrecords.entity.system.Role;
import com.jhaps.clientrecords.entity.system.User;

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
///*-----------------------ClientBin Conversion ---------------------------------------------------------------------------------------------------------------*/	
//	
//	public ClientBinDto toClientBinDto(ClientBin clientBin) {
//		ClientBinDto dto = new ClientBinDto();
//		dto.setId(clientBin.getId());
//		dto.setClientId(clientBin.getClientId());
//		dto.setFirstName(clientBin.getFirstName());
//		dto.setLastName(clientBin.getLastName());
//		dto.setDateOfBirth(clientBin.getDateOfBirth());
//		dto.setPostalCode(clientBin.getPostalCode());
//		return dto;
//	}
//	
//	
//	
//	public ClientBin toClientBinEntity(ClientBinDto clientBinDto) {
//		ClientBin entity = new ClientBin();
//		entity.setClientId(clientBinDto.getClientId());
//		entity.setFirstName(clientBinDto.getFirstName());
//		entity.setLastName(clientBinDto.getLastName());
//		entity.setDateOfBirth(clientBinDto.getDateOfBirth());
//		entity.setPostalCode(clientBinDto.getPostalCode());
//		return entity;
//	}
	
	
	
	
	
	
	
/*-----------------------USER---------------------------------------------------------------------------------------------------------------*/
	
	//USER ---> USER-DTO
	public UserDto toUserDto(User user) {
		UserDto dto = new UserDto();
				dto.setId(user.getId());
				dto.setEmail(user.getEmail());
				dto.setPassword(user.getPassword());
//				dto.setAttempts(user.getAttempts() != null ? user.getAttempts() : 0);
		return dto;
	}
	
	
	//USER-DTO ------> USER-ENTITY
	public User toUserEntity(UserDto userDto) {
		User user = new User();
			 user.setEmail(userDto.getEmail());
			 user.setPassword(userDto.getPassword());
//			 user.setAttempts(userDto.getAttempts());
		return user;	 
	}
	
	

	
///*-----------------------RoleDto-Set<Role> Not Role Entity---------------------------------------------------------------------------------------------------------------*/
//	
//	//Converting Set<Roles> to RoleDto.   **RoleDto-->RoleNames is of type Set<String>
//	public RoleDto toRoleDtoFromRoleSet(Set<Role> roleList) {
//		RoleDto dto = new RoleDto();
//		//collecting/extracting roles of Set<Role> roleList to Set<String>
//		Set<String> roles  = roleList.stream()
//								.map(role -> role.getName())
//								.collect(Collectors.toSet());
//		dto.setRoleNames(roles);
//		return dto;
//	}
//	
//	
//	
//	//Converting Single Role to RoleDto
//	public RoleDto toRoleDtoFromSingleRole(Role role) {
//		RoleDto dto = new RoleDto();
//		Set<String> roleSet = new HashSet<>();
//		
//		roleSet.add(role.getName());
//		dto.setRoleNames(roleSet);
//		return dto;
//	}
	
	
/*-----------------------IMAGE ENTITY-DTO ---------------------------------------------------------------------------------------------------------------*/	
	

//	/* ImageEntity to ImageDto*/
//	public ImageDto toImageDto(Image image) {
//		ImageDto dto = new ImageDto();
//		dto.setImageName(image.getImageName());
//		return dto;
//	}
//	
//	
//	/* ImageDto to ImageEntity */
//	public Image toImageEntity(ImageDto imageDto) {
//		Image entity = new Image();
//		entity.setImageName(imageDto.getImageName());
//		return entity;
//	}
//	
//	
	
	
}//ends class
