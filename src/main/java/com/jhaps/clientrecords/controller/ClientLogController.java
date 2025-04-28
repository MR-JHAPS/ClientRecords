package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.response.ClientLogResponse;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.client.ClientLogService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
/*
 * I have not used Method-Level Security-Authorization.
 * I have authenticated all controller apart from " AuthController ---> /api/public/** "
 * 
 * Because both "user" and "admin" should be able to use this controller.
 * 
 *  If I were to create a new Role then I will need to navigate to each 
 *  method to permit new role. So, I chose not to enable method-level-security.
 * 
 * */



@RestController
@RequestMapping("/api/client-logs")
@Tag(name = "Client-Log API's ",
		description = "This api containst the manipulation done to the client (deleted, updated, created Client Informations)")
@AllArgsConstructor
public class ClientLogController {

	private ClientLogService clientLogService;
	private ApiResponseBuilder apiResponseBuilder;
	private PagedResourceAssemblerService<ClientLogResponse> pagedResourceAssembler;
	
	
	
	@GetMapping
	@Operation(summary = "Get all the logs from the ClientLog.")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientLogResponse>>>> getAllClientLog(
				@RequestParam(defaultValue="0") int page,
				@RequestParam(defaultValue="10") int size,
				@RequestParam(required = false) String sortBy,
				@RequestParam(required = false) String direction){
		Pageable pageable =PageableUtils.createPageable(page, size, sortBy, direction);
		Page<ClientLogResponse> paginatedClientLog = clientLogService.getAllClientLog(pageable);
		PagedModel<EntityModel<ClientLogResponse>> pagedClientLog = pagedResourceAssembler.toPagedModel(paginatedClientLog);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientLog);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get the clientLog By Id.")
	public ResponseEntity<ApiResponseModel<ClientLogResponse>> getClientLogById(@PathVariable int id){
		ClientLogResponse clientLog = clientLogService.getClientLogById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientLog);
	}	
	
	
}//ends class
