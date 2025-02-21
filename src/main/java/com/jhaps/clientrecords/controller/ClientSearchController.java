package com.jhaps.clientrecords.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.dto.ApiResponse;
import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.ClientService;

@RestController
@RequestMapping("/api/clients/search")
public class ClientSearchController {

	private ClientService clientService;

	public ClientSearchController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	
	
	@GetMapping("/search/{searchQuery}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsBySearchQuery(@PathVariable String searchQuery){
		List<Client> clientList = clientService.findClientBySearchQuery(searchQuery);
		if(!clientList.isEmpty()) {
			ApiResponse<List<Client>> response = new ApiResponse<List<Client>>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else {
			ApiResponse<List<Client>> errorResponse = new ApiResponse<>(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	
	@GetMapping("/firstName/{firstName}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsByFirstName(@PathVariable String firstName){
		List<Client> clientList = clientService.findClientsByFirstName(firstName);
		if(!clientList.isEmpty()) {
			ApiResponse<List<Client>> response = new ApiResponse<List<Client>>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else {
			ApiResponse<List<Client>> errorResponse = new ApiResponse<>(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}	
	}
	
	
	@GetMapping("/lastName/{lastName}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsByLastName(@PathVariable String lastName){
		List<Client> clientList =  clientService.findClientsByLastName(lastName);
		if(!clientList.isEmpty()) {
			ApiResponse<List<Client>> response = new ApiResponse<List<Client>>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else {
			ApiResponse<List<Client>> errorResponse = new ApiResponse<>(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}
	}
	
	
	@GetMapping("/postalCode/{postalCode}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsByPostalCode(@PathVariable String postalCode){
		List<Client> clientList = clientService.findClientsByPostalCode(postalCode);	
		if(clientList.isEmpty()) {
			ApiResponse<List<Client>> response = new ApiResponse<List<Client>>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else{
			ApiResponse<List<Client>> errorResponse = new ApiResponse<>(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);	
		}
	}
	
	
	
//	@GetMapping("/dateOfBirth/{dateOfBirth}")
//	public ResponseEntity<List<Client>> getClientsByDateOfBirth(@PathVariable String dateOfBirth){
//		//Converting string date to LocalDate because service Layer parameter is LocalDate.
//		LocalDate parsedDate = LocalDate.parse(dateOfBirth);
//		List<Client> clientList = clientService.findClientsByDateOfBirth(parsedDate);
//		if(clientList.isEmpty()) {
//			return ResponseEntity.notFound().build();
//		}	
//		return ResponseEntity.ok(clientList);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//ends class.
