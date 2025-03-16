package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.service.ClientService;
import com.jhaps.clientrecords.service.PagedResourceAssemblerService;


@RestController
@RequestMapping("api/clients/sort")
public class ClientSortController {

	private ClientService clientService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ClientDto> pagedResourceAssemblerService;

	public ClientSortController(ClientService clientService, ApiResponseBuilder apiResponseBuilder
									,PagedResourceAssemblerService<ClientDto> pagedResourceAssemblerService) {
		this.clientService = clientService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
	}
	
//	
//	@GetMapping("/{firstName}")
//	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientDto>>>> sortByFirstName(Pageable pageable){
//		
//		List<ClientDto> clientList = clientService.sortClientByFirstNameAscending(null);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class
