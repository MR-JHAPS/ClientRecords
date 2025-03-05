package com.jhaps.clientrecords.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;


@Mapper(componentModel = "spring")
public interface ClientMapper {

	ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
	
	ClientDto toClientDto(Client clientEntity);
	
	Client toClientEntity(ClientDto clientDto);
	
	
}//ends interface


