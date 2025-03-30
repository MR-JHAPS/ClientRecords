package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.response.ClientBinDto;
import com.jhaps.clientrecords.service.client.ClientBinService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.SortBuilder;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/clientBin")
@AllArgsConstructor
public class ClientBinController {

	private ClientBinService clientBinService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ClientBinDto> pagedResourceAssembler;
	
	@GetMapping
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientBinDto>>>> getAllClientBin(Pageable pageable){
		
		pageable = pageable.getSort()?
		
		clientBinService.getAllClientBinClients(null);
		
	}
	
	
	
	
	
	
	
}
