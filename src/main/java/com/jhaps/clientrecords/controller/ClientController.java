package com.jhaps.clientrecords.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ApiResponse;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.ClientService;

import jakarta.validation.Valid;
/*This Contains The Basic CRUD Operations.*/

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	
	

	@GetMapping
	public ResponseEntity<ApiResponse<List<Client>>> getAllClients(){
		List<Client> clientList = clientService.findAllClients();
		if(clientList!=null && !clientList.isEmpty()) {
			ApiResponse<List<Client>> response = new ApiResponse<>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
			return ResponseEntity.ok(response);
		}else {
			ApiResponse<List<Client>> errorResponse = new ApiResponse<>(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}//ends method
	
	
	@PostMapping("/insert")
	public ResponseEntity<ApiResponse<String>> saveNewClient(@RequestBody Client client){
		try {
			clientService.saveClient(client);
			URI location = URI.create("/client/"+ client.getId());
			ApiResponse<String> response = new ApiResponse<String>(ResponseMessage.SUCCESS, HttpStatus.CREATED, "");
			return ResponseEntity.created(location).body(response);

		}catch(Exception e){
			ApiResponse<String> errorResponse = new ApiResponse<String>(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			
		}
	}
	
	
	//WHAT is the benefit of using the @valid and BindingResults here in this method and what will happen if i don't use it
	//STUDY THIS FURTHER FOR BETTER UNDERSTANDING.
	@DeleteMapping("/delete/id/{id}")
	public ResponseEntity<ApiResponse<String>> deleteClientById(@PathVariable int id){
		try {
			clientService.deleteClientById(id);
			ApiResponse<String> response = new ApiResponse<String>(ResponseMessage.SUCCESS, HttpStatus.NO_CONTENT);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}catch(Exception e) {
			ApiResponse<String> errorResponse = new ApiResponse<String>(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}//ends method
	
	


	
	
	
	
	
}//ends class
