package com.jhaps.clientrecords.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.service.ClientService;


@RestController
@RequestMapping("api/clients/filter")
public class ClientFilterController {

	private ClientService clientService;

	public ClientFilterController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	
	
	
	
	
	
	
	
	
}//ends class
