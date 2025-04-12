package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhaps.clientrecords.apiResponse.ApiResponseBuilder;
import com.jhaps.clientrecords.apiResponse.ApiResponseModel;
import com.jhaps.clientrecords.dto.request.ClientRequest;
import com.jhaps.clientrecords.dto.response.ClientResponse;
import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.service.client.ClientService;
import com.jhaps.clientrecords.service.system.PagedResourceAssemblerService;
import com.jhaps.clientrecords.util.PageableUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

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




@Validated // this is so that "@NotBlank" can be used in @pathVariable
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Client API's", description = "Create, Read, Update, Delete CLIENT-INFORMATION")
public class ClientController {

	
	private ApiResponseBuilder apiResponseBuilder;
	private ClientService clientService;
	private PagedResourceAssemblerService<ClientResponse> pagedResourceAssemblerService;
	

	public ClientController(ClientService clientService, ApiResponseBuilder apiResponseBuilder,
			PagedResourceAssemblerService<ClientResponse> pagedResourceAssemblerService) {
		this.clientService = clientService;
		this.apiResponseBuilder = apiResponseBuilder;
		this.pagedResourceAssemblerService = pagedResourceAssemblerService;
	}
//--------------------------------------------------------------------------------------------------------------------------------------------	
	
	
	@Operation(summary = "get all clients")
	@GetMapping
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientResponse>>>> getAllClients(
					@RequestParam(defaultValue = "0") int pageNumber,
					@RequestParam(defaultValue = "10") int pageSize,
					@RequestParam(required = false) String sortBy,
					@RequestParam(required = false) String direction){
			
		
		Pageable pageable =  PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		
		Page<ClientResponse> paginatedClients = clientService.findAllClients(pageable);
		//converting the clientList To PagedClientList
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	
	@Operation(summary = "get clients by id")
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseModel<ClientResponse>> getClientById( @PathVariable @Positive(message = "Id must be a positive number") int id){
		ClientResponse client = clientService.findClientById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, client);
	}
	
	
	@Operation(summary = "create new client")
	@PostMapping
	public ResponseEntity<ApiResponseModel<String>> saveNewClient( @RequestBody @Valid ClientRequest clientRequest,
			@AuthenticationPrincipal UserDetails userDetails){
		String userEmail = userDetails.getUsername();
		clientService.saveClient(userEmail, clientRequest);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "Client Data Created Successfully");
	}
	
	
	@Operation(summary = "delete client by id")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseModel<String>> deleteClientById(
			@PathVariable @Positive(message = "Id must be a positive number") int id,
			@AuthenticationPrincipal UserDetails userDetails){
			String userEmail = userDetails.getUsername();
			clientService.deleteClientById(userEmail, id);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.NO_CONTENT);
	}
	
	
	@Operation(summary = "update client by id")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseModel<String>> updateClientById(
							@PathVariable @Positive(message = "Id must be a positive number") int id,
							@RequestBody @Valid ClientRequest clientRequest,
							@AuthenticationPrincipal UserDetails userDetails){
			String userEmail = userDetails.getUsername();
			clientService.updateClientById(userEmail, id, clientRequest);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "Your Data is updated.");	
	}



	@Operation(summary = "Search Client API's", description = "Search by: firstName, lastName, postalCode, anyQuery")
	@GetMapping("/search")
	public ResponseEntity<ApiResponseModel<PagedModel<EntityModel<ClientResponse>>>> searchClientByQueryOrSpecificField(
							@RequestParam(required = false) String searchQuery,
							@RequestParam(required= false) String firstName,
							@RequestParam(required= false) String lastName,
							@RequestParam(required = false) String postalCode,
							@RequestParam(defaultValue ="0") int pageNumber,
							@RequestParam(defaultValue ="10") int pageSize,
							@RequestParam(required = false) String sortBy,
							@RequestParam(required = false) String direction){
		Pageable pageable =  PageableUtils.createPageable(pageNumber, pageSize, sortBy, direction);
		
		Page<ClientResponse>  paginatedClients; // creating an instance of Page<ClientResponse>.
		
		if(firstName!=null) {
		paginatedClients = clientService.findClientsByFirstName(firstName, pageable);
		}else if(lastName!=null) {
		paginatedClients = clientService.findClientsByLastName(lastName, pageable);
		}else if(postalCode!=null) {
		paginatedClients = clientService.findClientsByPostalCode(postalCode, pageable);
		}else {
		paginatedClients = clientService.findClientBySearchQuery(searchQuery, pageable);
		}
		
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	
	
	
	
	
}//ends class
