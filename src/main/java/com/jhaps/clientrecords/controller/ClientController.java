package com.jhaps.clientrecords.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

	private ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public ResponseEntity<?> getAllClients(){
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<Client> clientList = clientService.findAllClients();
		if(clientList!=null && !clientList.isEmpty()) {
			return new ResponseEntity<>(clientList,HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}//ends method
	
	
	
	@GetMapping("/firstName/{firstName}")
	public ResponseEntity<List<Client>> getClientsByFirstName(@PathVariable String firstName){
		
		List<Client> clientList = clientService.findClientsByFirstName(firstName);
		if(clientList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}else {
			return ResponseEntity.ok(clientList);
		}	
	}//ends method
	
	
	
	
	
}//ends class
