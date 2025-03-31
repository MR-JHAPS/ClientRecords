package com.jhaps.clientrecords.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.response.ClientBinDto;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.client.ClientBinService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;
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
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientBinDto>>>> getAllClientBin(
			@RequestParam(defaultValue="0") int pageNumber,
			@RequestParam(defaultValue="10") int pageSize,
			@RequestParam(required = false) String sortBy,
			@RequestParam(required = false) String direction){
		Pageable pageable = PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		Page<ClientBinDto> paginatedClient = clientBinService.getAllFromClientBin(pageable);
		PagedModel<EntityModel<ClientBinDto>> paged = pagedResourceAssembler.toPagedModel(paginatedClient);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, paged);
	}
	
	
	
	
	
	
	
}
