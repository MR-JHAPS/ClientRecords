package com.jhaps.clientrecords.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.service.ClientService;
import com.jhaps.clientrecords.util.ApiResponseBuilder;

@RestController
@RequestMapping("/api/clients/search")
public class ClientSearchController {	
	/*In the ApiResponseBuilder.class, responseEntity building method is created
		to reduce the boilerplate code
		no need to create :
						ApiResponse<List<Client>> response = new ApiResponse<>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
						return ResponseEntity.ok(response);
						for each (if/Else).
	*/
	private ApiResponseBuilder apiResponseBuilder;
	
	private ClientService clientService;

	public ClientSearchController(ClientService clientService, ApiResponseBuilder apiResponseBuilder) {
		this.clientService = clientService;
		this.apiResponseBuilder = apiResponseBuilder;
	}
	
	
	
	@GetMapping("/search/{searchQuery}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsBySearchQuery(@PathVariable String searchQuery){
		List<Client> clientList = clientService.findClientBySearchQuery(searchQuery);
		if(!clientList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/firstName/{firstName}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsByFirstName(@PathVariable String firstName){
		List<Client> clientList = clientService.findClientsByFirstName(firstName);
		if(!clientList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}	
	}
	
	
	@GetMapping("/lastName/{lastName}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsByLastName(@PathVariable String lastName){
		List<Client> clientList =  clientService.findClientsByLastName(lastName);
		if(!clientList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/postalCode/{postalCode}")
	public ResponseEntity<ApiResponse<List<Client>>> getClientsByPostalCode(@PathVariable String postalCode){
		List<Client> clientList = clientService.findClientsByPostalCode(postalCode);	
		if(!clientList.isEmpty()) {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
		}else {
			return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
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
