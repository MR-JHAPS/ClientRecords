package com.jhaps.clientrecords.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.jhaps.clientrecords.util.SortBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Validated // this is so that "@NotBlank" can be used in @pathVariable
@RestController
@RequestMapping("/api/clients")
@Tag(name = "6. Client Controller", description = "Create, Read, Update, Delete CLIENT-INFORMATION")
public class ClientController {
	/*In the ApiResponseBuilder.class, responseEntity building method is created
	to reduce the boilerplate code
	no need to create :
					ApiResponse<List<Client>> response = new ApiResponse<>(ResponseMessage.SUCCESS, HttpStatus.OK, clientList);
					return ResponseEntity.ok(response);
					for each Controller-method.
	 */
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
					@RequestParam(defaultValue = "0") int page,
					@RequestParam(defaultValue = "10") int size,
					@RequestParam(required = false) String sortBy,
					@RequestParam(required = false) String direction){
			
		//if "sortBy" and "Direction" parameters is null then "Sort sort" will return null.
		Sort sort = SortBuilder.createSorting(direction, sortBy);		
		//if sort returns null, don't apply sorting in PageRequest.
		Pageable pageable = (sort==null)? PageRequest.of(page, size) : PageRequest.of(page, size, sort);
		
		Page<ClientResponse> paginatedClients = clientService.findAllClients(pageable);
		//converting the clientList To PagedClientList
		PagedModel<EntityModel<ClientResponse>> pagedClientModel = pagedResourceAssemblerService.toPagedModel(paginatedClients);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, pagedClientModel);
	}
	
	@Operation(summary = "get clients by id")
	@GetMapping("/id/{id}")
	public ResponseEntity<ApiResponseModel<ClientResponse>> getClientById( @PathVariable @Positive(message = "Id must be a positive number") int id){
		ClientResponse client = clientService.findClientById(id);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, client);
	}
	
	
	@Operation(summary = "create new client")
	@PostMapping("/insert")
	public ResponseEntity<ApiResponseModel<String>> saveNewClient( @RequestBody @Valid ClientRequest clientRequest,
			@AuthenticationPrincipal UserDetails userDetails){
		String userEmail = userDetails.getUsername();
		clientService.saveClient(userEmail, clientRequest);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.CREATED, "Client Data Created Successfully");
	}
	
	
	@Operation(summary = "delete client by id")
	@DeleteMapping("/delete/id/{id}")
	public ResponseEntity<ApiResponseModel<String>> deleteClientById(
			@PathVariable @Positive(message = "Id must be a positive number") int id,
			@AuthenticationPrincipal UserDetails userDetails){
			String userEmail = userDetails.getUsername();
			clientService.deleteClientById(userEmail, id);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.NO_CONTENT);
	}
	
	
	@Operation(summary = "update client by id")
	@PutMapping("/update/id/{id}")
	public ResponseEntity<ApiResponseModel<String>> updateClientById(
							@PathVariable @Positive(message = "Id must be a positive number") int id,
							@RequestBody @Valid ClientRequest clientRequest,
							@AuthenticationPrincipal UserDetails userDetails){
			String userEmail = userDetails.getUsername();
			clientService.updateClientById(userEmail, id, clientRequest);
			return apiResponseBuilder.buildApiResponse(ResponseMessage.SUCCESS, HttpStatus.OK, "Your Data is updated.");	
	}



	
	
}//ends class
