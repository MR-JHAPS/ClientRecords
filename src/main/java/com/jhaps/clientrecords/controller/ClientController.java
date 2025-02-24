package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ClientDto;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.exception.EntityNotFoundException;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client Controller", description = "Create, Read, Update, Delete CLIENT-INFORMATION")
public class ClientController {
	/*In the ApiResponseBuilder.class, responseEntity building method is created
	to reduce the boilerplate code
	no need to create :
					ApiResponse<List<Client>> response = new ApiResponse<>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
					return ResponseEntity.ok(response);
					for each (if/Else).
	 */
	private ApiResponseBuilder apiResponseBuilder;
	private ClientService clientService;

	public ClientController(ClientService clientService, ApiResponseBuilder apiResponseBuilder) {
		this.clientService = clientService;
		this.apiResponseBuilder = apiResponseBuilder;
	}
//--------------------------------------------------------------------------------------------------------------------------------------------	
	
	
	@Operation(summary = "get all clients")
	@GetMapping
	public ResponseEntity<ApiResponse<Page<ClientDto>>> getAllClients( @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
		Pageable pageData = PageRequest.of(page, size);
		Page<ClientDto> clientList = clientService.findAllClients(pageData);
		if(clientList!=null && !clientList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Operation(summary = "create new client")
	@PostMapping("/insert")
	public ResponseEntity<ApiResponse<String>> saveNewClient(@RequestBody ClientDto client){
		try {
			clientService.saveClient(client);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "Client Data Created Successfully");
		}catch(Exception e){
			return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	
	//WHAT is the benefit of using the @valid and BindingResults here in this method and what will happen if i don't use it
	//STUDY THIS FURTHER FOR BETTER UNDERSTANDING.
	@Operation(summary = "delete client by id")
	@DeleteMapping("/delete/id/{id}")
	public ResponseEntity<ApiResponse<String>> deleteClientById(@PathVariable int id){
		try {
			clientService.deleteClientById(id);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.NO_CONTENT);
		}catch(Exception e) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@Operation(summary = "update client by id")
	@PutMapping("/update/id/{id}")
	public ResponseEntity<ApiResponse<Object>> updateClientById(@PathVariable int id, @Valid @RequestBody ClientDto clientUpdateInfo){
		try {
			clientService.updateClientById(id, clientUpdateInfo);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "Your Data is updated.");
		}catch(EntityNotFoundException e){
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}catch (Exception e) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
			
	}


	
	
	
	
	
}//ends class
