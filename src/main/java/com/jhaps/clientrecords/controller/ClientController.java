package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.exception.EntityNotFoundException;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.ClientService;
import com.jhaps.clientrecords.util.ApiResponseBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
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
	
	

	@GetMapping
	public ResponseEntity<ApiResponse<List<Client>>> getAllClients(){
		List<Client> clientList = clientService.findAllClients();
		if(clientList!=null && !clientList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PostMapping("/insert")
	public ResponseEntity<ApiResponse<String>> saveNewClient(@RequestBody Client client){
		try {
			clientService.saveClient(client);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, client.getFirstName());
		}catch(Exception e){
			return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);			
		}
	}
	
	
	//WHAT is the benefit of using the @valid and BindingResults here in this method and what will happen if i don't use it
	//STUDY THIS FURTHER FOR BETTER UNDERSTANDING.
	@DeleteMapping("/delete/id/{id}")
	public ResponseEntity<ApiResponse<String>> deleteClientById(@PathVariable int id){
		try {
			clientService.deleteClientById(id);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.NO_CONTENT);
		}catch(Exception e) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PutMapping("/update/id/{id}")
	public ResponseEntity<ApiResponse<Object>> updateClientById(@PathVariable int id, @Valid @RequestBody Client clientUpdateInfo){
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
