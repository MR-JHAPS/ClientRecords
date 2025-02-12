package com.jhaps.clientrecords.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.entity.Client;
import com.jhaps.clientrecords.service.ClientService;

import jakarta.validation.Valid;

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
	
	
	@GetMapping("/lastName/{lastName}")
	public ResponseEntity<List<Client>> getClientsByLastName(@PathVariable String lastName){
		
		List<Client> clientList =  clientService.findClientsByLastName(lastName);
		if(clientList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}	
		return ResponseEntity.ok(clientList);	
	}
	
	
	
	@GetMapping("/dateOfBirth/{dateOfBirth}")
	public ResponseEntity<List<Client>> getClientsByDateOfBirth(@PathVariable String dateOfBirth){
		//Converting string date to LocalDate because service Layer parameter is LocalDate.
		LocalDate parsedDate = LocalDate.parse(dateOfBirth);
		
		List<Client> clientList = clientService.findClientsByDateOfBirth(parsedDate);
		if(clientList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}	
		return ResponseEntity.ok(clientList);
	}
	
//	@PostMapping("/insert")
//	public ResponseEntity<?> saveNewClient(@Valid @RequestBody Client client, BindingResult result){
//		
//		if(result.hasErrors()) {
//			return ResponseEntity.badRequest().body("Invalid Client Date " + result.getAllErrors() );
//		}
//		try {
//			clientService.saveClient(client);
//			return ResponseEntity.status(HttpStatus.CREATED).body("New Client " + client.getFirstName() +"Saved successfully");
////			return ResponseEntity.accepted().body("Created successfully");
////			return ResponseEntity.created(null).build();
//		}catch(Exception e){
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the Client");
//			
//		}
//	}
	
	@PostMapping("/insert")
	public ResponseEntity<?> saveNewClient(@RequestBody Client client){
		
	
		try {
			clientService.saveClient(client);
			return ResponseEntity.status(HttpStatus.CREATED).body("New Client " + client.getFirstName() +"Saved successfully");
//			return ResponseEntity.accepted().body("Created successfully");
//			return ResponseEntity.created(null).build();
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the Client");
			
		}
	}
	
	//WHAT is the benefit of using the @valid and BindingResults here in this method and what will happen if i don't use it
	//STUDY THIS FURTHER FOR BETTER UNDERSTANDING.
	@DeleteMapping("/delete/id/{id}")
	public ResponseEntity<?> deleteClientById(@PathVariable int id){
		
		try {
			clientService.deleteClientById(id);
			return ResponseEntity.ok("Client Of Id " + id + " Deleted Successfully");
		}catch(Exception e) {
			return ResponseEntity.internalServerError().body("Unable To Delete The Client By Given Id At The Moment.");
		}
	}//ends method
	
	
	
	
	
}//ends class
